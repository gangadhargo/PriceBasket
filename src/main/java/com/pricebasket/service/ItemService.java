package com.pricebasket.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pricebasket.model.request.Item;
import com.pricebasket.model.response.ItemResponse;

/*
 * Item Service Layer
 */
@Component
public interface ItemService {
	public ItemResponse getAllItemsFromDB();
	public ItemResponse getItemsByItemNames(List<String> names);
	public ItemResponse createItem(Item item);
	public ItemResponse createBulkItems(String[] names, Double[] prices, Integer[] quantity);
}
