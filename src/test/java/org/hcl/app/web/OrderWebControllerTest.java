package org.hcl.app.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.business.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(OrderWebController.class)
public class OrderWebControllerTest {

	@MockBean
	private OrderService orderService;

	@Autowired
	private MockMvc mockMvc;

	private Sales sales = null;
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private List<Sale> createSaleItems() {
		List<Sale> saleItems = new ArrayList<Sale>();
		Sale sale = new Sale();
		sale.setProductName("Product 1");
		sale.setSaleId(1L);
		sale.setTotalAmount(10.00);
		sale.setTotalQty(10);
		saleItems.add(sale);
		
		return saleItems;
	}

	@Before
	public void init() throws ParseException {
		sales = new Sales();
		sales.setDate(DATE_FORMAT.parse("2020-01-01"));
		sales.setSalesId(1L);
	}

	@Test
	public void testGetOrder() throws Exception {
		APIResponse apiResponse = new APIResponse();
		apiResponse.setResponse(sales);
		BDDMockito.given(orderService.getOrderByNumber(1L)).willReturn(apiResponse);
		mockMvc.perform(get("/api/orders?orderid=1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("\"salesId\":1")));
	}

	@Test
	public void testCreateOrder() throws JsonProcessingException, Exception {
		APIResponse apiResponse = new APIResponse();
		apiResponse.setResponse(1L);
		sales.setSales(createSaleItems());
		BDDMockito.given(orderService.createOrder(ArgumentMatchers.any())).willReturn(apiResponse);
		mockMvc.perform(
			post("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(sales))
		)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("\"response\":1")));
	}
	
	@Test
	public void testDeleteOrder() throws Exception {
		APIResponse apiResponse = new APIResponse();
		apiResponse.setResponse(1L);
		BDDMockito.given(orderService.deleteOrder(1L)).willReturn(apiResponse);
		mockMvc.perform(delete("/api/orders?orderid=1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("\"response\":1")));
	}
	
	@Test
	public void testUpdateOrder() throws Exception {
		APIResponse apiResponse = new APIResponse();
		apiResponse.setResponse(1L);
		BDDMockito.given(orderService.updateOrder(ArgumentMatchers.any())).willReturn(apiResponse);
		mockMvc.perform(
			put("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(sales))
		)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("\"response\":1")));
	}
	
	@Test
	public void testGetSalesByDate() throws Exception {
		APIResponse apiResponse = new APIResponse();
		sales.setSales(createSaleItems());
		List<Sales> salesList = Arrays.asList(new Sales[] { sales });
		apiResponse.setResponse(salesList);
		BDDMockito.given(orderService.getOrdersByDate("2020-01-01")).willReturn(apiResponse);
		mockMvc.perform(get("/api/sales?date=2020-01-01"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("\"error\":false")));
	}
}
