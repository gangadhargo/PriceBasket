package com.pricebasket.exception;

import com.pricebasket.model.response.StatusMessage;

public class DBException extends Exception{
	public DBException(String s) {
		super(s);
	}
	
	public DBException(StatusMessage s) {
		super(s.getMessage());
	}
	
}
