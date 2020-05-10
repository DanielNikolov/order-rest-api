package org.hcl.app.data.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ORDERITEM")
public class OrderItem {

	@Id
	@Column(name = "ORDERITEM_ID")
	@SequenceGenerator(name= "ORDERITEM_SEQUENCE", sequenceName = "ORDERITEM_SEQUENCE_ID", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDERITEM_SEQUENCE")
	private long orderItemId;
	
	@Column(name = "ORDER_NUMBER")
	private long orderNumber;
	
	@Column(name = "PRODUCT_ID")
	private long productId;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@Column(name = "PRICE")
	private BigDecimal price;
	
	@OneToOne
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = false, updatable = false)
	private Product product;

	public long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
