package org.hcl.app.data.repository;

import java.util.List;

import org.hcl.app.data.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	Iterable<Product> findByNameIn(List<String> productNames);

}
