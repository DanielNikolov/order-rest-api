package org.hcl.app.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_PRODUCT")
public class Product {

	@Id
	@Column(name = "PRODUCT_ID")
	@SequenceGenerator(name= "CLIENT_SEQUENCE", sequenceName = "CLIENT_SEQUENCE_ID", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CLIENT_SEQUENCE")
	private long productId;
	
	@Column(name = "NAME")
	private String name;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
