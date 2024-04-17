package com.invento.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.invento.order.model.Order;

@Repository
public interface OrderRepo extends MongoRepository<Order, String>{

}
