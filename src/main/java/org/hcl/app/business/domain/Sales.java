package org.hcl.app.business.domain;

import java.util.Date;
import java.util.List;

public class Sales {
	private Date date;
	private List<Sale> sales;

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
