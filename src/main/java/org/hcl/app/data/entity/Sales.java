package org.hcl.app.data.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_SALES")
public class Sales {
	
	@Id
	@Column(name = "SALES_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long salesId;
	
	@Column(name = "SALES_DATE")
	private Date salesDate;
	
	@OneToMany(mappedBy = "salesId", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Sale> sales;

	public long getSalesId() {
		return salesId;
	}

	public void setSalesId(long salesId) {
		this.salesId = salesId;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}
}
