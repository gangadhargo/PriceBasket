package com.pricebasket.model.response;

import java.util.List;

import com.pricebasket.model.request.SpecialOffer;

/*
 * Special Offer Response class with its status
 */
public class SpecialOfferResponse extends StatusMessage{
	private List<SpecialOffer> data;
	

	public List<SpecialOffer> getData() {
		return data;
	}


	public void setData(List<SpecialOffer> data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "SpecialOfferResponse [data=" + data + "]";
	}
	
	
}
