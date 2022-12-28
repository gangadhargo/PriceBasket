package com.pricebasket.model.response;

import java.util.List;

import com.pricebasket.model.request.Item;

/*
 * Basket Response class with its status
 */
public class ItemResponse extends StatusMessage{
	private List<Item> data;

	public List<Item> getData() {
		return data;
	}

	public void setData(List<Item> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ItemResponse [data=" + data + "]";
	}
	
	
}
