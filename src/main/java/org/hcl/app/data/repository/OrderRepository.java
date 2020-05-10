package org.hcl.app.data.repository;

import java.sql.Date;

import org.hcl.app.data.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
	Iterable<Order> findByOrderDate(Date date);
}
