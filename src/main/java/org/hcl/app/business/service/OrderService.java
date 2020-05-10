package org.hcl.app.business.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hcl.app.business.adapters.OrderAdapter;
import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.data.entity.Order;
import org.hcl.app.data.entity.OrderItem;
import org.hcl.app.data.repository.OrderItemRepository;
import org.hcl.app.data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderAdapter orderAdapter;
	private final ProductService productService;

	@Autowired
	public OrderService(OrderRepository orderRepository,
			OrderAdapter orderAdapter,
			OrderItemRepository orderItemRepository,
			ProductService productService) {
		super();
		this.orderRepository = orderRepository;
		this.orderAdapter = orderAdapter;
		this.orderItemRepository = orderItemRepository;
		this.productService = productService;
	}
	
	/**
	 * updates order items using sales items passed
	 * @param saleItems sales items
	 * @param mapProductId map of product name: product id
	 * @throws Exception
	 */
	private void updateOrderItems(List<Sale> saleItems, Map<String, Long> mapProductId) throws Exception {
		try {
			saleItems.forEach(saleItem -> {
				try {
					OrderItem orderItem = this.orderItemRepository.findById(saleItem.getSaleId()).get();
					if (saleItem.getTotalAmount() != null) {
						orderItem.setPrice(new BigDecimal(saleItem.getTotalAmount()));
					}
					if (saleItem.getTotalQty() != null) {
						orderItem.setQuantity(saleItem.getTotalQty());
					}
					if (saleItem.getProductName() != null) {
						Long productId = mapProductId.get(saleItem.getProductName());
						if (productId == null) {
							throw new Exception("Incorrect product name");
						}
						orderItem.setProductId(productId);
					}
					this.orderItemRepository.save(orderItem);
				} catch (Exception ex) {
					throw new RuntimeException(ex.getMessage());
				}
			});
		} catch (RuntimeException ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * get a single order or all orders if no order number provided 
	 * @param orderNumber order number to be used for selection
	 * @return {@link APIResponse}
	 */
	public APIResponse getOrderByNumber(Long orderNumber) {
		APIResponse response = new APIResponse();
		List<Sales> salesList = new ArrayList<>();
		try {
			if (orderNumber != null) {
				Optional<Order> order = this.orderRepository.findById(orderNumber);
				if (!order.isPresent()) {
					throw new Exception("No corresponding order(s) found. Probably no valid order number provided");
				}
				salesList.add(this.orderAdapter.convertFromOrderEntity(order.get()));
			} else {
				salesList = this.orderAdapter.convertFromOrderEntities(this.orderRepository.findAll());
			}
			if (salesList.size() < 1) {
				throw new Exception("No corresponding order(s) found. Probably no valid order number provided");
			}
			response.setResponse(salesList);
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
			response.setResponse(orderAdapter.convertFromOrderEntity(order.get()));
			this.orderRepository.deleteById(orderId);
		} catch (Exception ex) {
			response.setError(true);
			response.setMessage(ex.getMessage());
		}
		
		return response;
	}
	
	public APIResponse createOrder(Sales sales) {
		APIResponse apiResponse = new APIResponse();
		try {
			Order order = this.orderRepository.save(this.orderAdapter.convertToOrderEntity(sales));
			if (sales.getSales().size() > 0) {
				List<OrderItem> entityOrderItems = this.orderAdapter.convertToOrderItemEntities(sales.getSales(),
						order.getOrderNumber(),
						this.productService.getProductsMapFromSales(sales.getSales()));
				this.orderItemRepository.saveAll(entityOrderItems);
			}
			apiResponse.setResponse(order.getOrderNumber());
		} catch (Exception ex) {
			apiResponse.setError(true);
			apiResponse.setMessage(ex.getMessage());
		}

		return apiResponse;
	}
	
	/**
	 * updates order data
	 * @param sales input data
	 * @return {@link APIResponse}
	 */
	public APIResponse updateOrder(Sales sales) {
		APIResponse apiResponse = new APIResponse();
		try {
			Order order = this.orderRepository.findById(sales.getSalesId()).get();
			if (null != sales.getDate()) {
				order.setOrderDate(new Date(sales.getDate().getTime()));
			}
			this.orderRepository.save(order);
			if (sales.getSales().size() > 0) {
				updateOrderItems(sales.getSales(),
						this.productService.getProductsMapFromSales(sales.getSales()));
			}
			apiResponse.setResponse(order.getOrderNumber());
		} catch (Exception ex) {
			apiResponse.setError(true);
			apiResponse.setMessage(ex.getMessage());
		}
		
		return apiResponse;
	}
	
	/**
	 * get sales by date if provided, otherwise return all sales
	 * @param dateString date
	 * @return {@link APIResponse}
	 */
	public APIResponse getOrdersByDate(String dateString) {
		APIResponse apiResponse = new APIResponse();
		try {
			Iterable<Order> orders = null;
			if (null != dateString) {
				orders = this.orderRepository.findByOrderDate(new Date(DATE_FORMAT.parse(dateString).getTime()));
			} else {
				orders = this.orderRepository.findAll();
			}
			List<Sales> salesList = this.orderAdapter.convertFromOrderEntities(orders);
			apiResponse.setResponse(salesList);
		} catch (Exception ex) {
			apiResponse.setError(true);
			apiResponse.setMessage(ex.getMessage());
		}
		
		return apiResponse;
	}
	
	
}
