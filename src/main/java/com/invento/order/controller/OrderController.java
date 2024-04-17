package com.invento.order.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invento.order.dto.OrderDto;
import com.invento.order.enums.OrderStatusValue;
import com.invento.order.model.Order;
import com.invento.order.service.OrderService;
import com.invento.order.util.Constants;

@RestController
@RequestMapping("order")
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/getOrderList")
	public ResponseEntity<List<Order>> getOrderList(
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER_VALUE) int pageNumber,
			@RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE_VALUE) int pageSize,
			@RequestParam(defaultValue = Constants.DEFAULT_UPDATED_DATE) String field,
      @RequestParam(defaultValue = Constants.DESC) Sort.Direction sortDirection) {

		List<Order> orders = orderService.getOrderList(pageNumber, pageSize,
				field, sortDirection);
		return (orders.size() > 0) 
				? ResponseEntity.status(HttpStatus.OK).body(orders)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(orders);
	}

	@GetMapping("/getOrderById")
	public ResponseEntity<Order> getOrderById(@RequestParam String id) {

		Optional<Order> orderOp = orderService.getOrderById(id);
		return (orderOp.isPresent()) 
				? ResponseEntity.status(HttpStatus.OK).body(orderOp.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Order());
	}

	@PostMapping("/createOrder")
	public ResponseEntity<String> createOrder(@RequestBody OrderDto dto) {

		return orderService.createOrder(dto)
				? ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully.")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create order.");
	}

	@PutMapping("/updateStatus")
	public ResponseEntity<String> updateOrder(@RequestParam String id, @RequestParam(defaultValue="confirmed") String orderStatus) {

		return orderService.updateOrderStatusById(id, orderStatus)
				? ResponseEntity.status(HttpStatus.OK).body("Order status updated.")
				: ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed to update order status.");
	}
	
	@GetMapping("/getStatusValues")
	public ResponseEntity<List<OrderStatusValue>> getOrderStatusValues(){
		
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderStatuses());
	}
}
