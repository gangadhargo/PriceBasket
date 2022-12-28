package com.pricebasket.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pricebasket.dao.SpecialOfferDao;
import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.request.ItemCategory;
import com.pricebasket.model.request.SpecialOffer;
import com.pricebasket.model.response.SpecialOfferResponse;
import com.pricebasket.service.SpecialOfferService;

import utils.StatusType;

@Component
public class SpecialOfferServiceImpl implements SpecialOfferService {
	
	private static Logger logger = LoggerFactory.getLogger(SpecialOfferServiceImpl.class);
	
	@Autowired
	SpecialOfferDao specialOfferDao;
	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	@Override
	public SpecialOfferResponse createSpecialOffer(SpecialOffer specialoffer) {
		// TODO Auto-generated method stub
		SpecialOfferResponse offerResponse = new SpecialOfferResponse();
		List<SpecialOffer> list = new ArrayList<>();
		if(specialoffer.getPercentage() < 0) {
			offerResponse.setMessage("Discount Percentage should be greater than 0");
			offerResponse.setType(StatusType.ERROR);
			return offerResponse;
		}
		SpecialOffer specOffer =  specialOfferDao.createSpecialOffersInDB(specialoffer);
		if(specOffer == null) {
			offerResponse.setMessage("Special Offer Already Existed in DB");
		}else {
			list.add(specOffer);
			offerResponse.setData(list);
		}
		return offerResponse;
	}
	@Override
	public SpecialOfferResponse givenListOfItemsGetSpecialOffers(List<Item> items) throws ServiceException {
		SpecialOfferResponse specialOfferResponse = new SpecialOfferResponse();
		try {

			List<SpecialOffer> applicableOffers = new ArrayList();
			for (SpecialOffer offer : specialOfferDao.getAllSpecialOffers()) {
				if (offer.getExipryDate() != null) {
					//If expire of discount is older than current date then ignore to add in applicable offers
					try {
						String offerDate = simpleDateFormat.format(offer.getExipryDate());
						String now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-YYY"));
						Date expiry = simpleDateFormat.parse(offerDate);
						Date current = simpleDateFormat.parse(now);
						if (expiry.before(current)) {
							continue;
						}
						logger.info("offerdate ", offerDate);

					} catch (ParseException e) {
						e.printStackTrace();
					}

				}
				Map<Item, Integer> map = offer.getDiscountAppliedOn();
				int isItTrueForEveryItemInMap = 0;
				for (Map.Entry<Item, Integer> entry : map.entrySet()) {
					for (Item item : items) {
						if (entry.getKey().getName().equals(item.getName())
								&& entry.getValue().equals(item.getQuantity())) {
							isItTrueForEveryItemInMap++;
						}
					}
				}
				if (isItTrueForEveryItemInMap == map.size()) {
					applicableOffers.add(offer);
				}
			}
			specialOfferResponse.setData(applicableOffers);
			System.out.println(applicableOffers);
		} catch (Exception e) {
			specialOfferResponse.setType(StatusType.ERROR);
			specialOfferResponse.setMessage(e.getMessage());
			logger.error(e.getMessage());
			throw new ServiceException(e.getMessage());
		}

		return specialOfferResponse;
    }
	
	/**
	 * Create Bulk Special offers in DB
	 * 
	 */
	@Override
	public SpecialOfferResponse createBulkSpecialOffers(List<LinkedHashMap<Item, Integer>> discountAppliedOn, String[] discountDescription,
			Double[] percentage) {
		SpecialOfferResponse specialOfferResponse = new SpecialOfferResponse();
		List<SpecialOffer> specialOffers = new ArrayList<>();
		for(int i=0; i<discountDescription.length; i++) {
			SpecialOffer offer = new SpecialOffer(discountDescription[i], discountAppliedOn.get(i), percentage[i]);
			specialOfferDao.createSpecialOffersInDB(offer);
			specialOffers.add(offer);
		}
		specialOfferResponse.setData(specialOffers);
		return specialOfferResponse;
	}
	
	
	@Override
	public SpecialOfferResponse createDiscountAtItemCategoryLevel(List<ItemCategory> itemCategories) {
		// TODO Auto-generated method stub
		return null;
	}

}
