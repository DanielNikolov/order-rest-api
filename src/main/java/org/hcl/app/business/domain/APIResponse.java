package org.hcl.app.business.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Order/Sales operation result")
public class APIResponse {
	@ApiModelProperty(notes = "Contains result of operation: order/sales/order id")
	private Object response;
	@ApiModelProperty(notes = "Error flag - true if error, false otherwise")
	private boolean error;
	@ApiModelProperty(notes = "Error message")
	private String message;

	public APIResponse() {
		this.response = null;
		this.error = false;
		this.message = "";
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
