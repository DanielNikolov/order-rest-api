package org.hcl.app.data.repository;

import java.sql.Date;

import org.hcl.app.data.entity.Sales;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends CrudRepository<Sales, Long> {
	Iterable<Sales> findBySalesDate(Date date);
}
