package com.pricebasket.model.request;


import java.text.DecimalFormat;
import java.util.Date;


/**
 *
 * class Item and its Properties
 */

public class Item  extends ItemCategory{
 
	private Long itemId;
    private String description;
    private String name;
    private Date itemExpiryDate;
    private int quantity;
    private Double price;
    public Item() {
    	
    }
    public Item(String name, Double price, Integer quantity) {
    	this.name = name;
    	this.price = price;
    	this.quantity = quantity;
    }
    
	public Long getItemId() {
		return itemId;
	}
	public String getDescription() {
		return description;
	}
	public String getName() {
		return name;
	}
	public Date getItemExpiryDate() {
		return itemExpiryDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setItemExpiryDate(Date itemExpiryDate) {
		this.itemExpiryDate = itemExpiryDate;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setPrice(Double price) {
		DecimalFormat df = new DecimalFormat("#.00");
		Double formattedDouble = Double.parseDouble(df.format(price));
		this.price = formattedDouble;
	}
	
}
