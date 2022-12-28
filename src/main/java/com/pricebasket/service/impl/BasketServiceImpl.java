package com.pricebasket.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pricebasket.PriceBasketApplication;
import com.pricebasket.dao.BasketDao;
import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Basket;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.request.SpecialOffer;
import com.pricebasket.model.response.BasketResponse;
import com.pricebasket.model.response.ItemResponse;
import com.pricebasket.model.response.PurchageAmountResponse;
import com.pricebasket.service.BasketService;
import com.pricebasket.service.ItemService;
import com.pricebasket.service.SpecialOfferService;

import utils.StatusType;

@Service
public class BasketServiceImpl implements BasketService {

	@Autowired
	ItemService itemService;

	@Autowired
	SpecialOfferService specialOfferService;

	@Autowired
	BasketDao basketDao;
	
	private static Logger logger = LoggerFactory.getLogger(BasketServiceImpl.class);
	
	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	/*
	 * Create Basket of Items
	 */
	@Override
	public BasketResponse createBasketWithItems(List<Item> items) throws ServiceException{
		BasketResponse basketResponse = new BasketResponse();
		List<Item> itemsFromDB = itemService.getAllItemsFromDB().getData();
		try {

		if (itemsFromDB.isEmpty() && itemsFromDB.size() == 0) {
			basketResponse.setMessage("Item list is empty add items into inventory later add into basket");
			basketResponse.setType(StatusType.ERROR);
			return basketResponse;
		} else {
			for (Item item : items) {
				boolean itemExists = false;
				for (Item itemDB : itemsFromDB) {
					if (item.getName().trim().equalsIgnoreCase(itemDB.getName().trim())) {
						itemExists = true;
					}
				}
				if (!itemExists) {
					basketResponse.setMessage("Given item is not existed in Inventory. Please add it to inventory");
					basketResponse.setType(StatusType.ERROR);
					return basketResponse;
				}
			}
		}
		
		Basket basket = basketDao.createBasket(items);
		List<Basket> data = new ArrayList<>();
		data.add(basket);
		basketResponse.setType(StatusType.SUCCESS);
		basketResponse.setStatusCode(200);
		basketResponse.setData(data);
		}catch(Exception e) {
			basketResponse.setMessage(e.getMessage());
			basketResponse.setType(StatusType.ERROR);
			throw new ServiceException(e.getMessage());
		}
		return basketResponse;

	}

	/*
	 * Give Basket id retrieve list of items in it
	 */
	public ItemResponse getAllListOfItemsForGivenBasketId(String basketId) throws ServiceException{
		ItemResponse itemResponse = new ItemResponse();
		try {
		Map<Basket, List<Item>> baskets = basketDao.getAllBaskets();
		if(baskets.size() == 0) {
			itemResponse.setMessage("given basket id not existed in system");
			itemResponse.setType(StatusType.ERROR);
			return itemResponse;
		}
		List<Item> items = new ArrayList<>();
		for (Map.Entry<Basket, List<Item>> entry : baskets.entrySet()) {
			if (entry.getKey().getId().equals(basketId)) {
				items = entry.getValue();
			}
		}
		itemResponse.setData(items);
		}catch (Exception e) {
			itemResponse.setMessage(e.getMessage());
			itemResponse.setType(StatusType.ERROR);
			throw new ServiceException(e.getMessage());
		}
		return itemResponse;
	}

	/*
	 * Give List of items calculate total amount after applying special offers / discounts
	 */
	public PurchageAmountResponse applyDiscountToBasketItemsAndCalculateTotalAmount(List<Item> items) throws ServiceException{
		PurchageAmountResponse purchageAmountResponse = new PurchageAmountResponse();
		if(items.isEmpty() || items.size() == 0) {
			purchageAmountResponse.setMessage("Basket is empty. Please add items into basket.");
			purchageAmountResponse.setType(StatusType.ERROR);
			return purchageAmountResponse;
		}
		try {
			
			Double subTotal = getProductsSum(items);
			Double totalDiscount = 0d;

			List<SpecialOffer> specialOffers = specialOfferService.givenListOfItemsGetSpecialOffers(items).getData();
			HashMap<String, String> discountNameAndAmount = new HashMap<>();
			
			//if no offers exists then return no offers available message else calculate discount with offers
			if (specialOffers.isEmpty() || specialOffers.size() == 0) {
				discountNameAndAmount.put("(No offers available)", "");
			} else {
				totalDiscount = calculateDiscount(specialOffers, items, discountNameAndAmount);
			}

			
			
			currencyFormat.setMinimumFractionDigits(2);
			purchageAmountResponse.setMessage("Total Amount Calculated");
			purchageAmountResponse.setType(StatusType.SUCCESS);
			purchageAmountResponse.setSubTotal(currencyFormat.format(subTotal));
			purchageAmountResponse.setDiscountNameAndAmount(discountNameAndAmount);
			purchageAmountResponse.setTotal(currencyFormat.format(subTotal - totalDiscount));
			
		} catch (Exception e) {
			purchageAmountResponse.setType(StatusType.ERROR);
			purchageAmountResponse.setMessage(e.getMessage());
			logger.error(e.getMessage());
		}
		return purchageAmountResponse;
	}
	/***
	 * 
	 * Apply Special offers to its item prices and calculate discount
	 */
	private Double calculateDiscount(List<SpecialOffer> specialOffers, List<Item> items, HashMap<String, String> discountNameAndAmount) {
		Double totalDiscount = 0d;
		for (SpecialOffer offer : specialOffers) {
			
			Map<Item, Integer> discountedProducts = offer.getDiscountAppliedOn();
			double productsSum = 0;
			//Iterating each product and apply discount if name and quantity is equals
			for (Map.Entry<Item, Integer> entry : discountedProducts.entrySet()) {
				for (Item item : items) {
					if (entry.getKey().getName().equals(item.getName())
							&& entry.getValue().equals(item.getQuantity())) {
						productsSum += item.getPrice();
					}
				}
				double amount = productsSum * (offer.getPercentage() / 100);
				DecimalFormat df = new DecimalFormat("#.00");
				Double discount = Double.parseDouble(df.format(amount));
				discountNameAndAmount.put(offer.getDescription(), discount < 1 ? currencyFormat.format(-discount).replace("£", "")+"p" : currencyFormat.format(-discount));
				totalDiscount += discount;
			}
		}
		
		return totalDiscount;
	}

	/**
	 * Get Items Total Price
	 * @param items
	 * @return total price
	 */
	private double getProductsSum(List<Item> items) {
		Double subTotal = 0d;
		for (Item item : items) {
			subTotal += item.getPrice();
		}
		return subTotal;
	}
}
