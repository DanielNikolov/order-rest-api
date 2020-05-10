package org.hcl.app.business.adapters;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.data.entity.Order;
import org.hcl.app.data.entity.OrderItem;
import org.hcl.app.data.entity.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderAdapterTest {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private Sales sales = null;
	private Sale saleItem = null;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private List<Order> orders = new ArrayList<Order>();
	private Map<String, Long> productIdMap = new HashMap<>();
	private OrderAdapter orderAdapter = new OrderAdapter(new OrderItemsAdapter());
	
	
	@Before
	public void init() throws ParseException {
		sales = new Sales();
		sales.setDate(DATE_FORMAT.parse("2020-01-01"));
		sales.setSalesId(1L);
		
		saleItem = new Sale();
		saleItem.setProductName("Product 1");
		saleItem.setSaleId(1L);
		saleItem.setTotalAmount(10.50);
		saleItem.setTotalQty(10);
		
		productIdMap.clear();
		productIdMap.put("Product 1", 1L);
		productIdMap.put("Product 2", 2L);
		productIdMap.put("Product 3", 3L);
		productIdMap.put("Product 4", 4L);
		productIdMap.put("Product 5", 5L);
		
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
		
		Order order = new Order();
		order.setOrderDate(new Date(DATE_FORMAT.parse("2020-01-01").getTime()));
		order.setOrderNumber(1L);
		order.setOrderItems(orderItems);
		orders.clear();
		orders.add(order);
	}
	
	@Test
	public void testConversionToOrderItemsEntitiesFail() {
		saleItem.setProductName("Product 0");
		List<Sale> saleItems = new ArrayList<>();
		saleItems.add(saleItem);
		
		try {
			orderAdapter.convertToOrderItemEntities(saleItems, sales.getSalesId(), productIdMap);
			Assert.fail("Invalid Product found. Product: Product 0");
		} catch (Exception ex) {
			
		}	
	}
	
	@Test
	public void testConversionToOrderItemsEntities() throws Exception {
		List<Sale> saleItems = new ArrayList<>();
		saleItems.add(saleItem);
		
		List<OrderItem> orderItems = orderAdapter.convertToOrderItemEntities(saleItems, sales.getSalesId(), productIdMap);
		Assert.assertEquals(saleItems.size(), orderItems.size());
		Assert.assertEquals(productIdMap.get(saleItem.getProductName()), (Long) orderItems.get(0).getProductId());
		Assert.assertEquals(1L, orderItems.get(0).getOrderNumber());
		Assert.assertEquals(saleItem.getTotalQty(), (Integer) orderItems.get(0).getQuantity());
		Assert.assertEquals(saleItem.getTotalAmount(), (Double) orderItems.get(0).getPrice().doubleValue());
	}
	
	@Test
	public void testConvertToOrderEntity() {
		Order order = orderAdapter.convertToOrderEntity(sales);
		Assert.assertEquals(sales.getSalesId(), (Long) order.getOrderNumber());
		Assert.assertEquals(sales.getDate().getTime(), order.getOrderDate().getTime());
	}
	
	@Test
	public void testConvertFromOrders() {
		List<Sales> salesList = orderAdapter.convertFromOrderEntities(orders);
		
		Assert.assertEquals(orders.size(), salesList.size());
		Assert.assertEquals((Long) orders.get(0).getOrderNumber(), salesList.get(0).getSalesId());
		Assert.assertEquals(orders.get(0).getOrderDate().getTime(), salesList.get(0).getDate().getTime());
		Assert.assertEquals(orders.get(0).getOrderItems().size(), salesList.get(0).getSales().size());
		Assert.assertEquals((Long) orders.get(0).getOrderNumber(), salesList.get(0).getSalesId());
		OrderItem orderItem = orders.get(0).getOrderItems().get(0);
		Sale saleItem = salesList.get(0).getSales().get(0);
		Assert.assertEquals((Long) orderItem.getOrderItemId(), saleItem.getSaleId());
		Assert.assertEquals(orderItem.getProduct().getName(), saleItem.getProductName());
	}
}
