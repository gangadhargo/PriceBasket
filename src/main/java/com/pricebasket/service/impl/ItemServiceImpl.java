package com.pricebasket.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pricebasket.dao.ItemDao;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.response.ItemResponse;
import com.pricebasket.service.ItemService;

import utils.StatusType;


@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemDao itemDao;


	@Override
	public ItemResponse getAllItemsFromDB() {
		ItemResponse itemResponse = new  ItemResponse();
		List<Item> items = itemDao.getAllItemsFromDB();
		itemResponse.setData(items);
		return itemResponse;
	}
	
	public ItemResponse getItemsByItemNames(List<String> names){
		ItemResponse itemResponse = new  ItemResponse();
		List<Item> items = itemDao.getItemsByItemNames(names);
		itemResponse.setData(items);
		return itemResponse;
	}

	@Override
	public ItemResponse createItem(Item item) {
		ItemResponse itemResponse = new ItemResponse();
		if(item.getPrice() > 0) {
			Item insertedItem = itemDao.createItem(item);
			List<Item> items = new ArrayList<>();
			items.add(insertedItem);
			itemResponse.setData(items);
		}else {
			itemResponse.setMessage("Item Price should be greater than 0");
			itemResponse.setType(StatusType.ERROR);
		}
		return itemResponse;
	}

	@Override
	public ItemResponse createBulkItems(String[] names, Double[] prices, Integer[] quantity) {
		ItemResponse itemResponse = new  ItemResponse();
		int itemsLength = names.length;
		List<Item> items = new ArrayList<>();
		for(int i=0; i<itemsLength; i++) {
			Item item = new Item(names[i].trim(), prices[i], quantity[i]);
			items.add(item);
		}
		List<Item> data = itemDao.createBulkItems(items);
		itemResponse.setData(data);
		return itemResponse;
	}

}
