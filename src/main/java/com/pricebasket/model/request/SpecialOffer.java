
package com.pricebasket.model.request;

import java.util.Date;

import java.util.Map;

/**
 *
 *	class Special offer and its properties
 */

public class SpecialOffer {
	private long id;
	private String couponCode;
	private Double percentage;
	private String description;
	private Map<Item, Integer> discountAppliedOn;
	private Map<ItemCategory, Integer> discountAtItemCategoryLevel;
	private Date exipryDate;

	public SpecialOffer() {

	}

	public Map<ItemCategory, Integer> getDiscountAtItemCategoryLevel() {
		return discountAtItemCategoryLevel;
	}

	public void setDiscountAtItemCategoryLevel(Map<ItemCategory, Integer> discountAtItemCategoryLevel) {
		this.discountAtItemCategoryLevel = discountAtItemCategoryLevel;
	}

	public SpecialOffer(String description, Map<Item, Integer> discountAppliedOn, Double percentage) {
		this.discountAppliedOn = discountAppliedOn;
		this.description = description;
		this.percentage = percentage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<Item, Integer> getDiscountAppliedOn() {
		return discountAppliedOn;
	}

	public void setDiscountAppliedOn(Map<Item, Integer> discountAppliedOn) {
		this.discountAppliedOn = discountAppliedOn;
	}


	public Date getExipryDate() {
		return exipryDate;
	}

	public void setExipryDate(Date exipryDate) {
		this.exipryDate = exipryDate;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	
	
}
