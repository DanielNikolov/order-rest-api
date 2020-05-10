package org.hcl.app.business.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Sale Item Data")
public class Sale {
	@ApiModelProperty(notes = "Sale Item ID")
	private Long saleId = null;
	@ApiModelProperty(notes = "Sale Item Product Name")
	private String productName = null;
	@ApiModelProperty(notes = "Sale Item Total Quantity")
	private Integer totalQty = null;
	@ApiModelProperty(notes = "Sale Item Total Price")
	private Double totalAmount = null;

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
