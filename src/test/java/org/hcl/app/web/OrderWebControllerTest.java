package org.hcl.app.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.hcl.app.business.domain.APIResponse;
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

	@Before
	public void init() throws ParseException {
		sales = new Sales();
		sales.setDate(DATE_FORMAT.parse("2020-01-01"));
		sales.setSalesId(1L);
	}

	@Test
	public void testGetOrder() throws Exception {
		APIResponse apiResponse = new APIResponse();
		sales.setSalesId(1L);
		apiResponse.setResponse(sales);
		BDDMockito.given(orderService.getOrderByNumber(1L)).willReturn(apiResponse);
		mockMvc.perform(get("/api/orders?orderid=1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("\"salesId\":1")));
	}

	@Test
	public void testCreateOrder() throws JsonProcessingException, Exception {
		APIResponse apiResponse = new APIResponse();
		apiResponse.setResponse(1);
		BDDMockito.given(orderService.createOrder(sales)).willReturn(apiResponse);
		mockMvc.perform(
				post("/api/orders")
				.content(new ObjectMapper().writeValueAsString(sales))
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk());
	}
}
