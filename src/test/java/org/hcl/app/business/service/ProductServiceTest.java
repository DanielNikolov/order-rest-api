package org.hcl.app.business.service;

import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hcl.app.business.domain.Sale;
import org.hcl.app.data.entity.Product;
import org.hcl.app.data.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	
	private List<Product> productsAll = new ArrayList<Product>();
	private List<Product> products = new ArrayList<Product>();
	private List<Sale> saleItems = new ArrayList<Sale>();
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService productService;
	
	private Product createProductEntity(String name, Long productId) {
		Product product = new Product();
		product.setName(name);
		product.setProductId(productId);
		
		return product;
	}
	
	@Before
	public void init() {
		productsAll.clear();
		productsAll.add(createProductEntity("Product 1", 1L));
		productsAll.add(createProductEntity("Product 2", 2L));
		productsAll.add(createProductEntity("Product 3", 3L));
		productsAll.add(createProductEntity("Product 4", 4L));
		productsAll.add(createProductEntity("Product 5", 5L));
		
		products.clear();
		products.add(createProductEntity("Product 1", 1L));
		
		saleItems.clear();
		Sale saleItem = new Sale();
		saleItem.setProductName("Product 1");
		saleItem.setTotalAmount(2.50);
		saleItem.setTotalQty(10);
		saleItems.add(saleItem);
	}
	
	@Test
	public void testProductConversion() {
		List<String> productNames = new ArrayList<String>();
		productNames.add("Product 1");
		Mockito.when(productRepository.findByNameIn(productNames)).thenReturn(products);
		lenient().when(productRepository.findAll()).thenReturn(productsAll);
		Map<String, Long> mapProductId = productService.getProductsMapFromSales(saleItems);
		
		Assert.assertFalse(mapProductId.isEmpty());
		Assert.assertNotNull(mapProductId.get(productNames.get(0)));
	}

}
