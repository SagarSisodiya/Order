package com.invento.order.mapper;

import org.springframework.stereotype.Component;

import com.invento.order.amqp.dto.OrderMQDto;
import com.invento.order.dto.OrderDto;
import com.invento.order.model.Order;

@Component
public class OrderMapper {

	public Order dtoToOrder(OrderDto dto) {

		Order order = new Order();
		order.setId(dto.getId());
		order.setProductId(dto.getProductId());
		order.setCustomerId(dto.getCustomerId());
		order.setQuantity(dto.getQuantity());
		return order;
	}

	public Order orderMQDtoToOrder(OrderMQDto dto) {

		Order order = new Order();
		order.setProductId(dto.getProductId());
		order.setCustomerId(dto.getCustomerId());
		order.setQuantity(dto.getQuantity());
		return order;
	}
}
