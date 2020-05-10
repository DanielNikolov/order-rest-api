package org.hcl.app.business.adapters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hcl.app.business.domain.Sale;
import org.hcl.app.data.entity.OrderItem;
import org.hcl.app.data.entity.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderItemsAdapterTest {

	private final OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter();
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private Sale saleItem = null;
	
	private Map<String, Long> productIdMap = null;
	
	@Before
	public void init() {
		Product product = new Product();
		product.setName("Product 1");
		product.setProductId(1L);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderItemId(1L);
		orderItem.setOrderNumber(1L);
		orderItem.setPrice(new BigDecimal(10.00));
		orderItem.setProductId(1L);
		orderItem.setQuantity(1);
		orderItem.setProduct(product);
		
		orderItems.clear();
		orderItems.add(orderItem);
		
		productIdMap = new HashMap<>();
		productIdMap.put("Product 1", 1L);
		productIdMap.put("Product 2", 2L);
		productIdMap.put("Product 3", 3L);
		productIdMap.put("Product 4", 4L);
		productIdMap.put("Product 5", 5L);
		
		saleItem = new Sale();
		saleItem.setProductName("Product 1");
		saleItem.setTotalAmount(2.50);
		saleItem.setTotalQty(10);
	}
	
	@Test
	public void testConvertFromOrderEntities() {
		List<Sale> saleItems = orderItemsAdapter.convertFromOrderItemEntities(orderItems);
		Assert.assertEquals(orderItems.size(), saleItems.size());
		Sale saleItem = saleItems.get(0);
		Assert.assertEquals(orderItems.get(0).getProduct().getName(), saleItem.getProductName());
		Assert.assertEquals((Long) orderItems.get(0).getOrderItemId(), saleItem.getSaleId());
	}
	
	@Test
	public void testConvertToOrderEntityListFail() {
		List<Sale> saleItems = new ArrayList<Sale>();
		saleItem.setProductName("Product 0");
		saleItems.add(saleItem);
		try {
			orderItemsAdapter.convertToOrderItemEntityList(saleItems, 1L, productIdMap);
			Assert.fail("Invalid Product found. Product: Product 0");
		} catch (Exception ex) {
			
		}
	}
	
	@Test
	public void testConvertToOrderEntityList() throws Exception {
		List<Sale> saleItems = new ArrayList<Sale>();
		saleItems.add(saleItem);
		List<OrderItem> orderItems = orderItemsAdapter.convertToOrderItemEntityList(saleItems, 1L, productIdMap);
		Assert.assertEquals(saleItems.size(), orderItems.size());
		Assert.assertEquals(productIdMap.get(saleItem.getProductName()), (Long) orderItems.get(0).getProductId());
		Assert.assertEquals(1L, orderItems.get(0).getOrderNumber());
		Assert.assertEquals(saleItem.getTotalQty(), (Integer) orderItems.get(0).getQuantity());
		Assert.assertEquals(saleItem.getTotalAmount(), (Double) orderItems.get(0).getPrice().doubleValue());
	}
}
