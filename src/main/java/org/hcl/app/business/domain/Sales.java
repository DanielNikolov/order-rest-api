package org.hcl.app.business.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sales {
	private Long salesId = null;
	private Date date;
	private List<Sale> sales = new ArrayList<Sale>();
	
	public Long getSalesId() {
		return salesId;
	}

	public void setSalesId(Long salesId) {
		this.salesId = salesId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}
}
