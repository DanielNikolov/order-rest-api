package org.hcl.app.business.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hcl.app.business.adapters.OrderAdapter;
import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.data.entity.Order;
import org.hcl.app.data.entity.OrderItem;
import org.hcl.app.data.entity.Product;
import org.hcl.app.data.repository.OrderItemRepository;
import org.hcl.app.data.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private OrderAdapter orderAdapter;
	
	@InjectMocks
	private OrderService orderService;
	
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private Order order = null;
	private Sales sales = null;
	private Map<String, Long> mapProductId = new HashMap<String, Long>();
	
	private OrderItem createOrderItem() {
		OrderItem orderItem = new OrderItem();
		Product product = new Product();
		product.setName("Product 1");
		product.setProductId(1L);
		
		orderItem.setOrderItemId(1L);
		orderItem.setOrderNumber(1L);
		orderItem.setPrice(new BigDecimal(10.00));
		orderItem.setProductId(1L);
		orderItem.setQuantity(10);
		orderItem.setProduct(product);
		
		return orderItem;
	}
	
	private Sale createSaleItem() {
		Sale sale = new Sale();
		sale.setProductName("Product 1");
		sale.setSaleId(1L);
		sale.setTotalAmount(10.00);
		sale.setTotalQty(10);
		
		return sale;
	}
	
	@Before
	public void init() throws ParseException {
		order = new Order();
		order.setOrderNumber(1L);
		order.setOrderDate(new Date(DATE_FORMAT.parse("2020-01-01").getTime()));
			
		sales = new Sales();
		sales.setDate(DATE_FORMAT.parse("2020-01-01"));
		sales.setSalesId(1L);
		
		mapProductId.clear();
		mapProductId.put("Product 1", 1L);
	}
	
	@Test
	public void testNoOrderFound() {
		Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());
		APIResponse apiResponse = orderService.getOrderByNumber(1L);
		Assert.assertTrue(apiResponse.isError());
	}
	
	@Test
	public void testGetOrder() {
		List<OrderItem> orderItems = Arrays.asList(new OrderItem[] {createOrderItem()});
		order.setOrderItems(orderItems);
		List<Sale> saleItems = Arrays.asList(new Sale[] { createSaleItem() });
		sales.setSales(saleItems);
		Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		Mockito.when(orderAdapter.convertFromOrderEntity(order)).thenReturn(sales);
		APIResponse apiResponse = orderService.getOrderByNumber(1L);
		
		Assert.assertFalse(apiResponse.isError());
		List<Sales> salesResponse = (List<Sales>) apiResponse.getResponse();
		Assert.assertEquals(new Long(1), salesResponse.get(0).getSalesId());
	}
	
	@Test
	public void testOrderSave() throws Exception {
		List<OrderItem> orderItems = Arrays.asList(new OrderItem[] { createOrderItem() });
		List<Sale> saleItems = Arrays.asList(new Sale[] { createSaleItem() });
		order.setOrderItems(orderItems);
		sales.setSales(saleItems);
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		Mockito.when(orderAdapter.convertToOrderEntity(sales)).thenReturn(order);
		Mockito.when(productService.getProductsMapFromSales(saleItems)).thenReturn(mapProductId);
		Mockito.when(orderAdapter.convertToOrderItemEntities(saleItems, 1L, mapProductId)).thenReturn(orderItems);
		Mockito.when(orderItemRepository.saveAll(orderItems)).thenReturn(orderItems);
		
		APIResponse apiResponse = orderService.createOrder(sales);
		
		Assert.assertFalse(apiResponse.isError());
		Assert.assertEquals((Long) order.getOrderNumber(), sales.getSalesId());
		
	}
	
	@Test
	public void testUpdateOrder() {
		Sale saleItem = createSaleItem();
		List<Sale> saleItems = Arrays.asList(new Sale[] { saleItem });
		sales.setSales(saleItems);
		OrderItem orderItem = createOrderItem();
		List<OrderItem> orderItems = Arrays.asList(new OrderItem[] { orderItem });
		order.setOrderItems(orderItems);
		Mockito.when(orderRepository.findById(sales.getSalesId())).thenReturn(Optional.of(order));
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		Mockito.when(productService.getProductsMapFromSales(saleItems)).thenReturn(mapProductId);
		Mockito.when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
		Mockito.when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
		
		APIResponse apiResponse = orderService.updateOrder(sales);
		
		Assert.assertFalse(apiResponse.isError());
		Assert.assertEquals(sales.getSalesId(), apiResponse.getResponse());
	}
	
	@Test
	public void testGetOrdersByDate() throws ParseException {
		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		List<Sales> salesList = new ArrayList<Sales>();
		salesList.add(sales);
		Mockito.when(orderRepository.findByOrderDate(new Date(DATE_FORMAT.parse("2020-01-01").getTime()))).thenReturn(orders);
		Mockito.when(orderAdapter.convertFromOrderEntities(orders)).thenReturn(salesList);
		APIResponse apiResponse = orderService.getOrdersByDate("2020-01-01");
		
		Assert.assertFalse(apiResponse.isError());
		Assert.assertNotNull(apiResponse.getResponse());
	}

}
