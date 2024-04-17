package com.invento.order.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order extends AuditLogging {
	
	private String id;
	
	private String productId;
	
	private long customerId;
	
	private int quantity;
	
	private List<OrderStatus> orderStatus = new ArrayList<>();
}
