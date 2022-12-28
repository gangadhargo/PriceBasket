package com.pricebasket.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.pricebasket.model.request.Basket;
import com.pricebasket.model.request.Item;

/**
 * 
 * Basket Data Access Layer
 * 
 * 
 * */
@Component
public interface BasketDao {
	
	public Basket createBasket(List<Item> items);
	public Map<Basket, List<Item>> getAllBaskets();
	public void removeItemsFromBasket(String basketId, List<Item> items);
}
