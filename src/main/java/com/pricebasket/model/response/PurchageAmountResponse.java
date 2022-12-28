package com.pricebasket.model.response;

import java.util.Map;
/*
 * PurchageAmount Response class with its status
 */
public class PurchageAmountResponse extends StatusMessage{
	private String subTotal;
	private Map<String, String> discountNameAndAmount;
	private String total;
	
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public Map<String, String> getDiscountNameAndAmount() {
		return discountNameAndAmount;
	}
	public void setDiscountNameAndAmount(Map<String, String> discountNameAndAmount) {
		this.discountNameAndAmount = discountNameAndAmount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	@Override
	public String toString() {
		
		String discountText = "";
		for(Map.Entry<String, String> discounts: discountNameAndAmount.entrySet()) {
			discountText+= discounts.getKey()+"      "+discounts.getValue()+"\n";
		}
		
		return  "Subtotal:            " + subTotal + "\n" 
		        + discountText+ 
		        "Total price:         " + total;
	}
	

}
