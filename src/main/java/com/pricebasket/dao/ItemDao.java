package com.pricebasket.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pricebasket.model.request.Item;


/**
 * Item Data Access Layer
 * */

@Component
public interface ItemDao {
    public Item createItem(Item item);
    public void updateItem(Item item);
    public void removeItemsFromDB(List<Item> items);
    public List<Item> getAllItemsFromDB();
    public List<Item> getItemsByItemNames(List<String> names);
    public List<Item> createBulkItems(List<Item> items);
}
