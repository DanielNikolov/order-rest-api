package org.hcl.app.business.adapters;

import java.util.ArrayList;
import java.util.List;

import org.hcl.app.business.domain.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleAdapter {

	private Sale convertSale(org.hcl.app.data.entity.Sale sale) {
		Sale result = new Sale();
		result.setProductName(sale.getProduct().getName());
		result.setTotalAmount(sale.getTotalAmount().doubleValue());
		result.setTotalQty(sale.getTotalQty());
		
		return result;
	}
	
	public List<Sale> convertSaleList(List<org.hcl.app.data.entity.Sale> saleList) {
		List<Sale> result = new ArrayList<Sale>();
		saleList.forEach(sale -> {
			result.add(convertSale(sale));
		});
		
		return result;
	}
}
