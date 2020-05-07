package org.hcl.app.data.repository;

import org.hcl.app.data.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
	Order findOrderByOrderNumber(Long orderNumber);
}
