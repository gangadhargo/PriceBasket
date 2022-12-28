package com.pricebasket.model.request;



import java.util.List;
import java.util.Map;

/**
 * class Basket and its Properties
 * 
 */

public class Basket{
    private String id;
    private List<Item> itemList;
    private double subTotal;
    private Map<String, Double> applicableDiscounts;
    private Double total;
    
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public Map<String, Double> getApplicableDiscounts() {
		return applicableDiscounts;
	}
	public void setApplicableDiscounts(Map<String, Double> applicableDiscounts) {
		this.applicableDiscounts = applicableDiscounts;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
    
    
   
    
    
}
