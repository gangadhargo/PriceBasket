package com.pricebasket.exception;

import com.pricebasket.model.response.StatusMessage;

public class ServiceException extends Exception{
	public ServiceException(String s) {
		super(s);
	}
	
	public ServiceException(StatusMessage s) {
		super(s.getMessage());
	}
	
}
