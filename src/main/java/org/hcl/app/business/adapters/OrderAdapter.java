package org.hcl.app.business.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hcl.app.business.domain.Order;
import org.hcl.app.business.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderAdapter {
	
	private final OrderItemsAdapter orderItemsAdapter;
	
	@Autowired
	public OrderAdapter(OrderItemsAdapter orderItemsAdapter) {
		super();
		this.orderItemsAdapter = orderItemsAdapter;
	}

	public Order convertOrder(org.hcl.app.data.entity.Order order) {
		Order result = new Order();
		result.setDate(new Date(order.getOrderDate().getTime()));
		result.setOrderNumber(order.getOrderNumber());
		List<OrderItem> orderItems = this.orderItemsAdapter.convertOrderItems(order.getOrderItems());
		result.setOrderItems(this.orderItemsAdapter.convertOrderItems(order.getOrderItems()));
		double totalAmt = orderItems.stream().map(orderItem -> orderItem.getPrice()).reduce(0.00, (price1, price2) -> price1 + price2);
		result.setTotalAmount(totalAmt);
		
		return result;
	}
	
	public List<Order> convertOrders(Iterable<org.hcl.app.data.entity.Order> orders) {
		List<Order> result = new ArrayList<Order>();
		orders.forEach(order -> {
			result.add(convertOrder(order));
		});
		
		return result;
	}
}
