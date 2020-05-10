package org.hcl.app.data.repository;

import org.hcl.app.data.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

}
