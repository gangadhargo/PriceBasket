package com.pricebasket.dao;


import java.util.List;

import org.springframework.stereotype.Component;

import com.pricebasket.model.request.SpecialOffer;


/**
 * Special Offer Data Access Layer
 * */
@Component
public interface SpecialOfferDao {
    SpecialOffer createSpecialOffersInDB(SpecialOffer specialOffer);

    void updateSpecialOfferInDB(SpecialOffer specialOffer);

    boolean removeSpecialOfferFromDB(SpecialOffer specialOffer);
    
    List<SpecialOffer> getAllSpecialOffers();
}
