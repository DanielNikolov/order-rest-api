package org.hcl.app.business.domain;

public class APIResponse {
	private Object response;
	private boolean error;
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
