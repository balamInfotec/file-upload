package com.serverless.model;

public class ApiResponse {
	/**
	 * Body
	 */
	private String body;
	
	/**
	 * Status code
	 */
	private int statusCode;

	public ApiResponse(String body, int statusCode) {
		super();
		this.body = body;
		this.statusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
