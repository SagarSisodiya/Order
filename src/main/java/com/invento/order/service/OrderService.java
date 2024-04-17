package com.invento.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.invento.order.amqp.dto.OrderMQDto;
import com.invento.order.dto.OrderDto;
import com.invento.order.enums.OrderStatusValue;
import com.invento.order.model.Order;

public interface OrderService {

	public List<Order> getOrderList(int pageNumber, int pageSize, String field, Sort.Direction sortDirection);

	public Optional<Order> getOrderById(String id);

	public boolean createOrder(OrderDto dto);

	public boolean updateOrderStatusById(String id, String orderStatus);

	public List<OrderStatusValue> getOrderStatuses();
	
	public void checkoutOrder(List<OrderMQDto> dtos);
}
