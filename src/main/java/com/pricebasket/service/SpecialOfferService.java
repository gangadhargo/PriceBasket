package com.pricebasket.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.request.ItemCategory;
import com.pricebasket.model.request.SpecialOffer;
import com.pricebasket.model.response.SpecialOfferResponse;

/*
 * Special Offer Service Layer
 */
@Component
public interface SpecialOfferService {
	SpecialOfferResponse givenListOfItemsGetSpecialOffers(List<Item> items) throws ServiceException;
	SpecialOfferResponse createSpecialOffer(SpecialOffer specialoffer);
	SpecialOfferResponse createBulkSpecialOffers(List<LinkedHashMap<Item, Integer>> discountAppliedOn, String[] discountDescription, Double[] percentage);
	SpecialOfferResponse createDiscountAtItemCategoryLevel(List<ItemCategory> itemCategories);
}
