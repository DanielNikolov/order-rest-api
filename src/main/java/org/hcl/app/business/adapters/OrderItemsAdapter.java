package org.hcl.app.business.adapters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hcl.app.business.domain.Sale;
import org.hcl.app.data.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemsAdapter {

	/**
	 * transforms Order Item entity to Sale Item business domain object
	 * @param orderItem Order Item entity
	 * @return {@link OrderItem}
	 */
	private Sale convertFromOrderItemEntity(OrderItem orderItem) {
		Sale saleItem = new Sale();
		saleItem.setProductName(orderItem.getProduct().getName());
		saleItem.setTotalAmount(orderItem.getPrice().doubleValue());
		saleItem.setTotalQty(orderItem.getQuantity());
		saleItem.setSaleId(orderItem.getOrderItemId());
		
		return saleItem;
	}
	
	/**
	 * transforms list of order item entites to sale item domain objects
	 * @param orderItems list of order item entities
	 * @return {@link List}
	 */
	public List<Sale> convertFromOrderItemEntities(List<OrderItem> orderItems) {
		List<Sale> saleItems = new ArrayList<Sale>();
		orderItems.forEach(orderItem -> {
			saleItems.add(convertFromOrderItemEntity(orderItem));
		});
		
		return saleItems;
	}
	
	/**
	 * transforms sale item domain object to order item entity
	 * @param saleItem sale item domain object
	 * @return {@link OrderItem}
	 * @throws Exception 
	 */
	private OrderItem convertToOrderItemEntity(Sale saleItem, Long orderNumber, Map<String, Long> productIdMap) throws Exception {
		OrderItem result = new OrderItem();
		result.setPrice(new BigDecimal(saleItem.getTotalAmount()));
		Long productId = productIdMap.get(saleItem.getProductName());
		if (productId == null) {
			throw new Exception(String.format("Invalid Product found. Product: %s", saleItem.getProductName()));
		}
		result.setProductId(productId);
		result.setQuantity(saleItem.getTotalQty());
		result.setOrderNumber(orderNumber);
		if (saleItem.getSaleId() != null) {
			result.setOrderItemId(saleItem.getSaleId());
		}
		
		return result;
	}
	
	/**
	 * transforms sale item domain objects to list of order item entities
	 * @param orderItems list of order item domain objects to be transformed
	 * @return {@link List}
	 */
	public List<OrderItem> convertToOrderItemEntityList(List<Sale> saleItems, Long orderNumber, Map<String, Long> productIdMap) throws Exception {
		List<OrderItem> result = new ArrayList<>();
		for (Sale saleItem : saleItems) {
			result.add(convertToOrderItemEntity(saleItem, orderNumber, productIdMap));
		}
		
		return result;
	}
}
