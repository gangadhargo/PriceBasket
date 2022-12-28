package com.pricebasket.model.response;

import java.util.List;

import com.pricebasket.model.request.Basket;

/*
 * Basket Response class with its status
 */
public class BasketResponse extends StatusMessage{
	private List<Basket> data;

	public List<Basket> getData() {
		return data;
	}

	public void setData(List<Basket> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BasketResponse [data=" + data + "]";
	}
	
	
}
