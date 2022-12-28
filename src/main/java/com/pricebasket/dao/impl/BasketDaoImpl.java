package com.pricebasket.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pricebasket.dao.BasketDao;
import com.pricebasket.model.request.Basket;
import com.pricebasket.model.request.Item;

@Repository
public class BasketDaoImpl implements BasketDao{

	private Map<Basket, List<Item>> basketToItemsDB = new HashMap<>();
	
	/**
	 * Given a list of items create Basket
	 * 
	 */
	
	@Override
	public Basket createBasket(List<Item> items) {
		String basketId = UUID.randomUUID().toString(); 
		Basket basket = new Basket();
		basket.setId(basketId);
		basket.setItemList(items);
		//Adding items to Basket DB
		basketToItemsDB.put(basket, items);
		return basket;
	}
	
	public Map<Basket, List<Item>> getAllBaskets(){
		return basketToItemsDB;
	}
	
	/**
	 * Remove Items from Bakset DB based on basket Id
	 */

	@Override
	public void removeItemsFromBasket(String basketId, List<Item> items) {
		// TODO Auto-generated method stub
		for(Map.Entry<Basket, List<Item>> entry: basketToItemsDB.entrySet()) {
			if(entry.getKey().getId().equals(basketId)) {
				List<Item> allItems = entry.getKey().getItemList();
				allItems.removeAll(items);
				basketToItemsDB.put(entry.getKey(), allItems);
			}
		}
	}
	
}
