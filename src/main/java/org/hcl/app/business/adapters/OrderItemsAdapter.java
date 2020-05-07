package org.hcl.app.business.adapters;

import java.util.ArrayList;
import java.util.List;

import org.hcl.app.business.domain.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemsAdapter {

	private OrderItem convertOrderItem(org.hcl.app.data.entity.OrderItem orderItem) {
		OrderItem result = new OrderItem();
		result.setQuantity(orderItem.getQuantity());
		result.setPrice(orderItem.getPrice().doubleValue());
		result.setProductName(orderItem.getProduct().getName());
		result.setProductId(orderItem.getProductId());
		
		return result;
	}
	
	public List<OrderItem> convertOrderItems(List<org.hcl.app.data.entity.OrderItem> orderItems) {
		List<OrderItem> result = new ArrayList<OrderItem>();
		orderItems.forEach(orderItem -> {
			result.add(convertOrderItem(orderItem));
		});
		
		return result;
	}
}
