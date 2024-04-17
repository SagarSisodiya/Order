package com.invento.order.config;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.invento.order.amqp.dto.OrderMQDto;
import com.invento.order.service.OrderService;

@Configuration
public class RMQConfig {

	private OrderService orderService;
	
	private final Logger log = LoggerFactory.getLogger(RMQConfig.class);
	
	RMQConfig(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@Bean
	Consumer<List<OrderMQDto>> checkOutOrder() {
		
		return (value) -> {
			log.info("Received message: {}", value);
			orderService.checkoutOrder(value);
		};
	}
}
