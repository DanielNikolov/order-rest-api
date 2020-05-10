package org.hcl.app.web;

import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.business.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("/api/orders")
@Api(value = "HCL Order REST API application", description = "Performs simple CRUD operations in order context")
public class OrderWebController implements ErrorController {

	private final OrderService orderService;

	@Autowired
	public OrderWebController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}
	
	@GetMapping("/api/orders")
	@ApiOperation(value = "Get order by order id",
		httpMethod = "GET", 
		produces = "application/json",
		response = APIResponse.class)
	public APIResponse getOrder(@RequestParam(name = "orderid", required = false) Long orderId) {
		return this.orderService.getOrderByNumber(orderId);
	}
	
	@PostMapping("/api/orders")
	@ApiOperation(value = "Creates new order",
		httpMethod = "POST",
		consumes = "application/json",
		produces = "application/json",
		response = APIResponse.class)
	public APIResponse createOrder(@RequestBody(required = true) Sales sales) {
		APIResponse apiResponse = this.orderService.createOrder(sales);
		return apiResponse;
	}

	@PutMapping("/api/orders")
	@ApiOperation(value = "Updates existing order and order items",
		httpMethod = "PUT",
		consumes = "application/json",
		produces = "application/json",
		response = APIResponse.class)
	public APIResponse updateOrder(@RequestBody(required = true) Sales sales) {
		return this.orderService.updateOrder(sales);
	}
	
	@DeleteMapping("/api/orders")
	@ApiOperation(value = "Deletes existing order or all orders if no order id specified",
		httpMethod = "DELETE",
		produces = "application/json",
		response = APIResponse.class)
	public APIResponse deleteOrder(@RequestParam(name = "orderid", required = false) Long orderId) {
		return this.orderService.deleteOrder(orderId);
	}
	
	@GetMapping("/api/sales")
	@ApiOperation(value = "Get sales by date or all sales if no date is specified",
		httpMethod = "GET",
		produces = "application/json",
		response = APIResponse.class)
	public APIResponse getSales(@RequestParam(name = "date", required = false) String dateString) {
		return this.orderService.getOrdersByDate(dateString);
	} 
	
	@GetMapping("/error")
	public APIResponse handleError() {
		APIResponse response = new APIResponse();
		response.setError(true);
		response.setMessage("Error processing request");
		
		return response;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	
}
