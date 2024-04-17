package com.invento.order.enums;

public enum OrderStatusValue {

	PENDING("pending"),
	CONFIRMED("confirmed"),
	IN_TRANSIT("in transit"),
	RETURNED("returned"),
	REORDERED("reordered"),
	DELETED("deleted");
	
	private String value;
	
	OrderStatusValue(String value){
		this.value = value;
	};
	
	@Override
	public String toString() {
		return value;
	}
}
