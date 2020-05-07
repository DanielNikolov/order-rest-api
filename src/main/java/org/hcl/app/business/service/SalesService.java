package org.hcl.app.business.service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hcl.app.business.adapters.SalesAdapter;
import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.domain.Sale;
import org.hcl.app.business.domain.Sales;
import org.hcl.app.data.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesService {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private final SalesRepository salesRepository;
	private final SalesAdapter salesAdapter;

	@Autowired
	public SalesService(SalesRepository salesRepository,
			SalesAdapter salesAdapter) {
		super();
		this.salesRepository = salesRepository;
		this.salesAdapter = salesAdapter;
	}
	
	public APIResponse getSales(String dateString) {
		APIResponse response = new APIResponse();
		try {
			Iterable<org.hcl.app.data.entity.Sales> sales = null;
			if (null != dateString) {
				sales = this.salesRepository.findBySalesDate(new Date(DATE_FORMAT.parse(dateString).getTime()));
			} else {
				sales = this.salesRepository.findAll();
			}
			List<Sales> salesList = this.salesAdapter.convertSalesList(sales);
			if (salesList.size() < 1) {
				throw new Exception("No sales found");
			}
			response.setResponse(salesList);
		} catch (Exception ex) {
			response.setError(true);
			response.setMessage(ex.getMessage());
		}
		
		return response;
	}
	
	
	
}
