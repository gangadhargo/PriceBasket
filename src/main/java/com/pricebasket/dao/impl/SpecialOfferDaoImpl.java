package com.pricebasket.dao.impl;

import com.pricebasket.dao.SpecialOfferDao;
import com.pricebasket.model.request.SpecialOffer;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;

@Repository
public class SpecialOfferDaoImpl implements SpecialOfferDao {

    private static List<SpecialOffer> specialOfferInDB = new ArrayList<>();

    /**
     * Create a Special offer in DB
     */
    @Override
    public SpecialOffer createSpecialOffersInDB(SpecialOffer specialOffer) {
    	if(!checkIfDiscountAlreadyExistsInDB(specialOffer)) {
    		specialOfferInDB.add(specialOffer);
    		return specialOffer;
    	}
    	return null;
    }
    
    /**
     * Given Special Offer is already exists in DB or not
     * @param specialOffer
     * @return true or false
     */
	private boolean checkIfDiscountAlreadyExistsInDB(SpecialOffer specialOffer) {
		boolean isDiscountExistsInDB = false;
		if(specialOfferInDB.isEmpty() || specialOfferInDB.size() == 0) return false;
		for (SpecialOffer offer : specialOfferInDB) {
			if (offer.getDiscountAppliedOn().equals(specialOffer.getDiscountAppliedOn())) {
				isDiscountExistsInDB = true;
			}
		}
		return isDiscountExistsInDB;
	}

	/**
	 * 
	 * update special offer in DB
	 */
    @Override
    public void updateSpecialOfferInDB(SpecialOffer specialOffer){
    	int i = 0;
    	for (SpecialOffer offer : specialOfferInDB) {
    		if(offer.getDiscountAppliedOn().equals(specialOffer.getDiscountAppliedOn())) {
    			specialOfferInDB.set(i, specialOffer);
    			break;
    		}
    		i++;
    	}
    }
    /**
     * 
     * Remove Special offer from DB
     */

    @Override
    public boolean removeSpecialOfferFromDB(SpecialOffer specialOffer) {
    	boolean isSpecialOfferRemoved = false;
    	for (SpecialOffer offer : specialOfferInDB) {
    		if(offer.getDiscountAppliedOn().equals(specialOffer.getDiscountAppliedOn())) {
    			specialOfferInDB.remove(specialOffer);
    			isSpecialOfferRemoved = true;
    			break;
    		}
    	}
        return isSpecialOfferRemoved;
    }
    
    /**
     * 
     * Getting all special offer from DB
     */

	@Override
	public List<SpecialOffer> getAllSpecialOffers() {
		return specialOfferInDB;
	}
    
}
