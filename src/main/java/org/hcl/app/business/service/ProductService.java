package org.hcl.app.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hcl.app.business.domain.Sale;
import org.hcl.app.data.entity.Product;
import org.hcl.app.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}
	
	/**
	 * create products map: Product Name -> Product Id using specified names list or all products if no list specified
	 * @return {@link Map}
	 */
	private Map<String, Long> getProductsMapByNames(List<String> productNames) {
		Map<String, Long> result = new HashMap<String, Long>();
		Iterable<Product> products = productNames != null && productNames.size() > 0 ? productRepository.findByNameIn(productNames) : productRepository.findAll();
		products.forEach(product -> {
			result.put(product.getName(), product.getProductId());
		});

		return result;
	}
	
	/**
	 * creates map <product_name>:<product_id> from all sale items
	 * @param saleItems list of sale items to create map for
	 * @return {@link Map} created map
	 */
	public Map<String, Long> getProductsMapFromSales(List<Sale> saleItems) {
		List<String> productNames = new ArrayList<String>();
		saleItems.forEach(saleItem -> {
			productNames.add(saleItem.getProductName());
		});
		return getProductsMapByNames(productNames);
	}	
}
