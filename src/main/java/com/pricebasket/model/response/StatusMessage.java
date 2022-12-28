package com.pricebasket.model.response;



import utils.StatusType;
/*
 * Status class with its properties
 */
public class StatusMessage {
	private Integer statusCode;
	private String message;
	private StatusType type;
	
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public StatusType getType() {
		return type;
	}
	public void setType(StatusType type) {
		this.type = type;
	}
	
	
	
}
