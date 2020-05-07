package org.hcl.app.business.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hcl.app.business.domain.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesAdapter {
	
	private final SaleAdapter saleAdapter;
	
	@Autowired
	public SalesAdapter(SaleAdapter saleAdapter) {
		super();
		this.saleAdapter = saleAdapter;
	}

	public Sales convertSales(org.hcl.app.data.entity.Sales sales) {
		Sales result = new Sales();
		result.setDate(new Date(sales.getSalesDate().getTime()));
		result.setSales(this.saleAdapter.convertSaleList(sales.getSales()));
		
		return result;
	}
	
	public List<Sales> convertSalesList(Iterable<org.hcl.app.data.entity.Sales> salesList) {
		List<Sales> result = new ArrayList<Sales>();
		salesList.forEach(sales -> {
			result.add(convertSales(sales));
		});
		
		return result;
	}
}
