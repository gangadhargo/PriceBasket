package com.pricebasket.dao.impl;

import com.pricebasket.dao.ItemDao;
import com.pricebasket.model.request.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 
 * Class to create, delete, update, get item 
 * */
@Repository
public class ItemDaoImpl implements ItemDao {

	private static Map<String, Item> itemNametoItemMap = new HashMap<>();

	/**
	 * 
	 * Creating Item 
	 * */
    public Item createItem(Item item) {
    	if(!itemNametoItemMap.containsKey(item.getName()) || (itemNametoItemMap.get(item.getName()).getQuantity()!= item.getQuantity())){
    		itemNametoItemMap.put(item.getName(), item);
    		return item;
    	}
    	return null;
    }

    /**
	 * 
	 * Updating Item 
	 * */
    @Override
    public void updateItem(Item item) {
      if(itemNametoItemMap.containsKey(item.getName())) {
    	  itemNametoItemMap.put(item.getName(), item);
      }
    }
    
    /**
   	 * 
   	 * Getting Item by item name
   	 * */
    public Item getItemByItemName(String name) {
        return itemNametoItemMap.get(name);
    }	
    /**
   	 * 
   	 * Getting Bulk Items by item names
   	 * */
    public List<Item> getItemsByItemNames(List<String> names) {
    	List<Item> itemsList = new ArrayList<>(); 
    	for(String name: names) {
    		if(itemNametoItemMap.containsKey(name.trim())) {
    			itemsList.add(itemNametoItemMap.get(name));
    		}
    	}
        return itemsList;
    }
    
    /**
     * Get all List of items from DB
     */
    public List<Item> getAllItemsFromDB() {
    	List<Item> items = List.copyOf(itemNametoItemMap.values());
        return items;
    }

    /**
     * Given a list of items remove items from item DB
     */
    public void removeItemsFromDB(List<Item> items) {
        if (!items.isEmpty() && items.size() != 0) {
           for(Item item: items) {
        	   itemNametoItemMap.remove(item.getName());
           }
        }
    }
    
    /**
     * Create Bulk items from DB
     */

	@Override
	public List<Item> createBulkItems(List<Item> items) {
		for(Item item: items) {
			if(!itemNametoItemMap.containsKey(item.getName())){
	    		itemNametoItemMap.put(item.getName(), item);
	    	}
		}
		return items;
	}
}
