package com.pricebasket.model.response;

import java.util.List;

import com.pricebasket.model.request.ItemCategory;

/*
 * ItemCategory Response class with its status
 */
public class ItemCategoryResponse extends StatusMessage{
	
	List<ItemCategory> data;

	public List<ItemCategory> getData() {
		return data;
	}

	public void setData(List<ItemCategory> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ItemCategoryResponse [data=" + data + "]";
	}
	

}
