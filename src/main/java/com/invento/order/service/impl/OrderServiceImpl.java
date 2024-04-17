package com.invento.order.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.invento.order.amqp.dto.OrderMQDto;
import com.invento.order.dto.OrderDto;
import com.invento.order.enums.OrderStatusValue;
import com.invento.order.mapper.OrderMapper;
import com.invento.order.model.Order;
import com.invento.order.model.OrderStatus;
import com.invento.order.repository.OrderRepo;
import com.invento.order.service.OrderService;

import io.micrometer.common.util.StringUtils;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepo orderRepo;

	private OrderMapper orderMapper;

	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

	public OrderServiceImpl(OrderRepo orderRep, OrderMapper orderMapper) {
		this.orderRepo = orderRep;
		this.orderMapper = orderMapper;
	}

	@Override
	public List<Order> getOrderList(int pageNumber, int pageSize,
			String field, Sort.Direction sortDirection) {
		List<Order> orders = new ArrayList<>();
		try {
			Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sortDirection, field);
			orders = orderRepo.findAll(pageRequest).toList();
		} catch (Exception e) {
			log.error("Could not found orders.");
		}
		return orders;
	}

	@Override
	public Optional<Order> getOrderById(String id) {
		Optional<Order> orderOp = Optional.empty();
		try {
			orderOp = orderRepo.findById(id);
			if (orderOp.isEmpty()) {
				throw new Exception("Order not found with id: " + id);
			}
		} catch (Exception e) {
			log.error("Error: {}", id);
		}
		return orderOp;
	}

	@Override
	@Transactional
	public boolean createOrder(OrderDto dto) {
		boolean created = false;
		try {
			Order order = orderMapper.dtoToOrder(dto);
			order.getOrderStatus().add(new OrderStatus(OrderStatusValue.CONFIRMED.toString(), LocalDateTime.now()));
			order = orderRepo.save(order);
			if (StringUtils.isBlank(order.getId())) {
				throw new Exception("Failed to create order.");
			}
			created = true;
			log.info("Order created. Order Details: {}", order);
		} catch (Exception e) {
			log.error("Error: {}", e.getMessage());
		}
		return created;
	}

	@Override
	@Transactional
	public boolean updateOrderStatusById(String id, String orderStatus) {
		boolean updated = false;
		try {
			Optional<Order> orderOp = orderRepo.findById(id);
			if (orderOp.isPresent()) {
				Order order = orderOp.get();
				addCurrentStatus(order, orderStatus);
				orderRepo.save(order);
				updated = true;
			} else {
				log.error("Order not found with id: {}", id);
			}
		} catch (Exception e) {
			log.error("Error deleting order with id: {}. Error: {}", id, e.getMessage());
		}
		return updated;
	}

	private void addCurrentStatus(Order order, String orderStatus) {

		if (Objects.nonNull(order) && StringUtils.isNotBlank(orderStatus)) {
			order.getOrderStatus().add(
					new OrderStatus(orderStatus, LocalDateTime.now()));
		} else {
			log.error("Order/OrderStatus not found");
		}
	}

	@Override
	public List<OrderStatusValue> getOrderStatuses() {
		
		return Stream.of(OrderStatusValue.values()).toList();
	}

	@Override
	public void checkoutOrder(List<OrderMQDto> dtos) {
		List<Order> orders = new ArrayList<>();
		try {
			if(CollectionUtils.isEmpty(dtos)) {
				throw new Exception("Dto is null/empty.");
			}
			dtos.stream().forEach(o -> {
				Order order = orderMapper.orderMQDtoToOrder(o);
				order.getOrderStatus().add(new OrderStatus(OrderStatusValue.CONFIRMED.toString(), LocalDateTime.now()));
				orders.add(order);
			});
			orderRepo.saveAll(orders);
			log.info("Orders created.");
		} catch(Exception e) {
			log.error("Error: {}", e.getMessage());
		}
		
	}
}
