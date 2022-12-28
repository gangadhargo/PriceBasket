package com.pricebasket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.response.BasketResponse;
import com.pricebasket.model.response.PurchageAmountResponse;
import com.pricebasket.service.BasketService;
import com.pricebasket.service.ItemService;
import com.pricebasket.service.SpecialOfferService;



@Configuration
@ComponentScan
@SpringBootApplication
public class PriceBasketApplication {
	
	private static Logger logger = LoggerFactory.getLogger(PriceBasketApplication.class);
	private static ItemService itemService;
	private static SpecialOfferService specialOfferService;
	private static BasketService basketService; 
	
	private static final Properties env = new Properties();
	private static InputStream inputStream = PriceBasketApplication.class.getClassLoader().getResourceAsStream("application.properties");
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PriceBasketApplication.class, args);
		basketService = context.getBean(BasketService.class);
		itemService = context.getBean(ItemService.class);
		specialOfferService = context.getBean(SpecialOfferService.class);
		createItems();
		createSpecialOffers();
	
		try {
			List<String> input = Arrays.asList(args);
			//Getting data from terminal and look in database, return if data exists. 
			List<Item> itemsFromDB = itemService.getItemsByItemNames(input).getData();
			//Creating Basket with terminal Items
			BasketResponse basket = basketService.createBasketWithItems(itemsFromDB);
			//Applying discount and calculate total
			if(basket.getData().get(0)!= null) {
				PurchageAmountResponse amountResponse =  basketService.applyDiscountToBasketItemsAndCalculateTotalAmount(basket.getData().get(0).getItemList());
				logger.info("\n"+amountResponse.toString());
			}
			
		} catch (ServiceException e) {
			logger.error("Issue while calculating discount");
		}
		
	}
	
	/**
	 * 
	 * Creating Special Offer in DB by reading data from application.properties
	 * */
	private static void createSpecialOffers() {
		try {
			env.load(inputStream);
			String[] discountItems = env.getProperty("discountItems").split(",");
			String[] discountDescription = env.getProperty("discountDescription").split(",");
			String[] discountPercentage = env.getProperty("discountPercentage").split(",");
			String[] discountItemQuantity = env.getProperty("discountItemQuantity").split(",");
			
			Double[] percentages = Arrays.stream(discountPercentage).map(Double::valueOf).toArray(Double[]::new);
			Integer[] quantites = Arrays.stream(discountItemQuantity).map(Integer::valueOf).toArray(Integer[]::new);
			List<Item> items = itemService.getItemsByItemNames(Arrays.asList(discountItems)).getData();
			List<LinkedHashMap<Item, Integer>> discounts = new ArrayList<>();
			
			for(int i=0; i<items.size(); i++) {
				LinkedHashMap<Item, Integer> discountApplied = new LinkedHashMap<>();
				discountApplied.put(items.get(i), quantites[i]);
				discounts.add(discountApplied);
			}
			specialOfferService.createBulkSpecialOffers(discounts, discountDescription, percentages);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/**
	 * 
	 * Creating Items in DB by reading data from application.properties
	 * */
	
	private static void createItems() {
		
		try {
			env.load(inputStream);
			String[] itemNames = env.getProperty("itemNames").split(",");
			String[] itemPrices = env.getProperty("itemPrices").split(",");
			String[] itemQuantity = env.getProperty("itemQuantity").split(",");
			Double[] prices = Arrays.stream(itemPrices).map(Double::valueOf).toArray(Double[]::new);
			Integer[] quantites = Arrays.stream(itemQuantity).map(Integer::valueOf).toArray(Integer[]::new);
			List<Item> itemResponse = itemService.createBulkItems(itemNames, prices, quantites).getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
