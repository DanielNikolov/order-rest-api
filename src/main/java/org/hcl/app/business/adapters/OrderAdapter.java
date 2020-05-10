package org.hcl.app.business.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.data.entity.Order;
import org.hcl.app.data.entity.OrderItem;
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

	/**
	 * transforms order entity to sales domain object
	 * @param order order entity
	 * @return {@link Order}
	 */
	public Sales convertFromOrderEntity(Order order) {
		Sales sales = new Sales();
		sales.setDate(new Date(order.getOrderDate().getTime()));
		List<Sale> saleItems = this.orderItemsAdapter.convertFromOrderItemEntities(order.getOrderItems());
		sales.setSales(saleItems);
		sales.setSalesId(order.getOrderNumber());
		//double totalAmt = saleItems.stream().map(saleItem -> saleItem.getTotalAmount()).reduce(0.00, (price1, price2) -> price1 + price2);
		
		return sales;
	}
	
	/**
	 * transforms list of order entity to list of sale domain objects
	 * @param orders list of order entities
	 * @return {@link List}
	 */
	public List<Sales> convertFromOrderEntities(Iterable<Order> orders) {
		List<Sales> salesList = new ArrayList<Sales>();
		orders.forEach(order -> {
			salesList.add(convertFromOrderEntity(order));
		});
		
		return salesList;
	}
	
	/**
	 * transforms sales domain object to order entity
	 * @param sales sales domain object
	 * @return {@link Order} order entity
	 */
	public Order convertToOrderEntity(Sales sales) {
		Order result = new Order();
		result.setOrderDate(new java.sql.Date(sales.getDate().getTime()));
		if (sales.getSalesId() != null) {
			result.setOrderNumber(sales.getSalesId());
		}
		
		return result;
	}
	
	/**
	 * transforms sale items to order items entities
	 * @param salesItems sale items
	 * @param orderNumber order number
	 * @param productIdMap product name -> product id map
	 * @return {@link List} order item entities
	 * @throws Exception
	 */
	public List<OrderItem> convertToOrderItemEntities(List<Sale> salesItems,
			Long orderNumber, Map<String, Long> productIdMap) throws Exception {
		return this.orderItemsAdapter.convertToOrderItemEntityList(salesItems, orderNumber, productIdMap);
	}
}
