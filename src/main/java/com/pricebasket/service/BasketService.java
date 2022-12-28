package com.pricebasket.service;

import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.response.BasketResponse;
import com.pricebasket.model.response.ItemResponse;
import com.pricebasket.model.response.PurchageAmountResponse;

import java.util.List;

import org.springframework.stereotype.Component;

/*
 * Basket Service Layer
 */
@Component
public interface BasketService {
	public BasketResponse createBasketWithItems(List<Item> items) throws ServiceException;
	public ItemResponse getAllListOfItemsForGivenBasketId(String basketId) throws ServiceException;
	public PurchageAmountResponse applyDiscountToBasketItemsAndCalculateTotalAmount(List<Item> items) throws ServiceException;
}
