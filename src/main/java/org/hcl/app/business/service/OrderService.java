package org.hcl.app.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hcl.app.business.adapters.OrderAdapter;
import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.data.entity.Order;
import org.hcl.app.data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderAdapter orderAdapter;

	@Autowired
	public OrderService(OrderRepository orderRepository,
			OrderAdapter orderAdapter) {
		super();
		this.orderRepository = orderRepository;
		this.orderAdapter = orderAdapter;
	}
	
	/**
	 * get a single order or all orders if no order number provided 
	 * @param orderNumber order number to be used for selection
	 * @return {@link APIResponse}
	 */
	public APIResponse getOrderByNumber(Long orderNumber) {
		APIResponse response = new APIResponse();
		List<org.hcl.app.business.domain.Order> orders = new ArrayList<>();
		try {
			if (orderNumber != null) {
				Optional<Order> order = this.orderRepository.findById(orderNumber);
				if (!order.isPresent()) {
					throw new Exception("No corresponding order is found. Probably no valid order number provided");
				}
				orders.add(this.orderAdapter.convertOrder(order.get()));
			} else {
				orders = this.orderAdapter.convertOrders(this.orderRepository.findAll());
			}
			response.setResponse(orders);
		} catch (Exception ex) {
			response.setError(true);
			response.setMessage(ex.getMessage());
		}
		
		return response;
	}
	
	/**
	 * deletes order specified by order id
	 * @param orderId order id of the order to be deleted
	 * @return {@link APIResponse}
	 */
	public APIResponse deleteOrder(Long orderId) {
		APIResponse response = new APIResponse();
		try {
			Optional<Order> order = this.orderRepository.findById(orderId);
			if (!order.isPresent()) {
				throw new Exception("No corresponding order is found. Probably no valid order number provided");
			}
			response.setResponse(orderAdapter.convertOrder(order.get()));
			this.orderRepository.deleteById(orderId);
		} catch (Exception ex) {
			response.setError(true);
			response.setMessage(ex.getMessage());
		}
		
		return response;
	}
}
