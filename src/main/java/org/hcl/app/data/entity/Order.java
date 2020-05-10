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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_ORDER")
public class Order {
	
	@Id
	@Column(columnDefinition = "ORDER_NUMBER")
	@SequenceGenerator(name= "ORDER_SEQUENCE", sequenceName = "ORDER_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDER_SEQUENCE")
	private long orderNumber;
	
	@Column(name = "ORDER_DATE")
	private Date orderDate;
	
	@OneToMany(mappedBy = "orderNumber", 
			cascade = {CascadeType.MERGE, CascadeType.REMOVE}, 
			orphanRemoval = true)
	private List<OrderItem> orderItems;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
