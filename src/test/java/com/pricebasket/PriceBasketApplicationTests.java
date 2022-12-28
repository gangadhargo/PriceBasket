package com.pricebasket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.pricebasket.dao.SpecialOfferDao;
import com.pricebasket.exception.ServiceException;
import com.pricebasket.model.request.Item;
import com.pricebasket.model.request.SpecialOffer;
import com.pricebasket.model.response.BasketResponse;
import com.pricebasket.model.response.ItemResponse;
import com.pricebasket.model.response.PurchageAmountResponse;
import com.pricebasket.model.response.SpecialOfferResponse;
import com.pricebasket.service.ItemService;
import com.pricebasket.service.SpecialOfferService;
import com.pricebasket.service.impl.BasketServiceImpl;
import com.pricebasket.service.impl.ItemServiceImpl;
import com.pricebasket.service.impl.SpecialOfferServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(OrderAnnotation.class)
class PriceBasketApplicationTests {

	@InjectMocks
	private BasketServiceImpl basketService;

	@InjectMocks
	private SpecialOfferServiceImpl specialOfferServiceImpl;

	@Mock
	private SpecialOfferService offerService;

	@InjectMocks
	private ItemServiceImpl itemServiceImpl;

	@Mock
	private ItemService itemService;

	@Mock
	private SpecialOfferDao specialOfferDao;

	List<Item> items = new ArrayList<>();

	SpecialOfferResponse specialOfferResponse = new SpecialOfferResponse();

	@Test
	@Order(1)
	@DisplayName("Calculate Basket total without special offer")
	public void totalAmountWithOutOffer() throws ServiceException {
		createItem();
		List<SpecialOffer> offer = new ArrayList<>();
		SpecialOfferResponse specialOfferResponse = new SpecialOfferResponse();
		specialOfferResponse.setData(offer);
		when(offerService.givenListOfItemsGetSpecialOffers(items)).thenReturn(specialOfferResponse);
		PurchageAmountResponse purchageAmountResponse = basketService
				.applyDiscountToBasketItemsAndCalculateTotalAmount(items);
		Map<String, String> discountMap = purchageAmountResponse.getDiscountNameAndAmount();
		assertTrue(discountMap.containsKey("(No offers available)"));
		assertEquals(purchageAmountResponse.getTotal(), "£10.00");
	}

	@Test
	@Order(2)
	@DisplayName("Calculate Basket total with spcial offer")
	public void totalAmountWithOffer() throws ServiceException {
		createItem();
		items.get(0).setQuantity(2);
		createSpecialOffer();
		when(offerService.givenListOfItemsGetSpecialOffers(items)).thenReturn(specialOfferResponse);
		PurchageAmountResponse purchageAmountResponse = basketService
				.applyDiscountToBasketItemsAndCalculateTotalAmount(items);
		Map<String, String> discountMap = purchageAmountResponse.getDiscountNameAndAmount();

		assertTrue(discountMap.containsKey("Oranges 10% off"));
		assertEquals(purchageAmountResponse.getTotal(), "£9.00");
	}
	
	@Test
	@Order(3)
	@DisplayName("Calculate Basket total with spcial offer")
	public void calculateTotalAmountWithMultipleItemsAndOffers() throws ServiceException {
		createItem();
		items.get(0).setQuantity(2);
		Item item = new Item();
		item.setName("Kiwi");
		item.setQuantity(2);
		item.setPrice(5d);
		items.add(item);
		createSpecialOffer();
		SpecialOffer offer = new SpecialOffer();
		Map<Item, Integer> map = new HashMap<>();
		map.put(items.get(0), 2);
		offer.setDiscountAppliedOn(map);
		offer.setPercentage(10d);
		offer.setDescription("kiwi 10% off");
		specialOfferResponse.getData().add(offer);
		when(offerService.givenListOfItemsGetSpecialOffers(items)).thenReturn(specialOfferResponse);
		PurchageAmountResponse purchageAmountResponse = basketService
				.applyDiscountToBasketItemsAndCalculateTotalAmount(items);
		Map<String, String> discountMap = purchageAmountResponse.getDiscountNameAndAmount();

		assertEquals(purchageAmountResponse.getSubTotal(), "£15.00");
		assertTrue(discountMap.containsKey("Oranges 10% off"));
		assertTrue(discountMap.containsKey("kiwi 10% off"));
		assertEquals(purchageAmountResponse.getTotal(), "£13.00");
	}

	@Test
	@Order(4)
	@DisplayName("Test Empty Basket error message while calculating basket total")
	public void emptyBasketErrorMessage() throws ServiceException {
		List<Item> items = new ArrayList<Item>();
		PurchageAmountResponse purchageAmountResponse = basketService
				.applyDiscountToBasketItemsAndCalculateTotalAmount(items);
		assertEquals(purchageAmountResponse.getMessage(), "Basket is empty. Please add items into basket.");
	}

	@Test
	@Order(5)
	@DisplayName("testing null pointer exception thrown on null data while creating basket")
	public void nullItemTestWhileCreatingBasket() throws ServiceException {
		assertThrows(NullPointerException.class, () -> {
			basketService.createBasketWithItems(null);
		});
	}

	@Test
	@Order(6)
	@DisplayName("Test error meesage while create Basket with empty items")
	public void createBasketWithEmptyItemsTest() throws ServiceException {
		ItemResponse itemResponse = new ItemResponse();
		List<Item> items = new ArrayList<>();
		itemResponse.setData(items);
		when(itemService.getAllItemsFromDB()).thenReturn(itemResponse);
		BasketResponse basketResponse = basketService.createBasketWithItems(items);
		assertEquals(basketResponse.getMessage(), "Item list is empty add items into inventory later add into basket");
	}

	@Test
	@Order(7)
	@DisplayName("If Offer has old expiry date then dont add it to applicable offers")
	public void specialOfferWithOldExpiryDate() throws ServiceException, ParseException {
		createItem();
		createSpecialOffer();
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = sf.parse("25-12-2022");
		specialOfferResponse.getData().get(0).setExipryDate(date);

		when(specialOfferDao.getAllSpecialOffers()).thenReturn(specialOfferResponse.getData());
		SpecialOfferResponse offerResponse = specialOfferServiceImpl.givenListOfItemsGetSpecialOffers(items);
		assertTrue(offerResponse.getData().isEmpty());
	}

	@Test
	@Order(8)
	@DisplayName("If Offer has greater than current date of expiry date then add it to applicable offers")
	public void specialOfferWithGreaterThanCurrentExpiryDate() throws ServiceException, ParseException {
		createItem();
		createSpecialOffer();
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = sf.parse("02-02-2024");
		specialOfferResponse.getData().get(0).setExipryDate(date);
		Map<Item, Integer> map = new HashMap<>();
		map.put(items.get(0), 1);
		specialOfferResponse.getData().get(0).setDiscountAppliedOn(map);
		when(specialOfferDao.getAllSpecialOffers()).thenReturn(specialOfferResponse.getData());
		SpecialOfferResponse offerResponse = specialOfferServiceImpl.givenListOfItemsGetSpecialOffers(items);
		assertFalse(offerResponse.getData().isEmpty());
	}

	@Test
	@Order(9)
	@DisplayName("Register an item with 0 or negative price")
	public void createItemWithZeroOrNegativePrice() {
		createItem();
		items.get(0).setPrice(0d);
		ItemResponse itemResponse = itemServiceImpl.createItem(items.get(0));
		assertEquals("Item Price should be greater than 0", itemResponse.getMessage());
		
	}
	
	@Test
	@Order(10)
	@DisplayName("Test Discount Percentage greater than zero error message")
	public void discountPercentageGreaterthanZero() {
		createItem();
		createSpecialOffer();
		List<SpecialOffer> offers = specialOfferResponse.getData();
		offers.get(0).setPercentage(-1);
		SpecialOfferResponse specialOfferResponse = specialOfferServiceImpl.createSpecialOffer(offers.get(0));
		assertEquals("Discount Percentage should be greater than 0", specialOfferResponse.getMessage());
		
	}
	
	private void createItem() {
		Item item = new Item();
		item.setName("Oranges");
		item.setQuantity(1);
		item.setPrice(10d);
		items.add(item);
	}
	
	private void createSpecialOffer() {
		SpecialOffer offer = new SpecialOffer();
		Map<Item, Integer> map = new HashMap<>();
		map.put(items.get(0), 2);
		offer.setDiscountAppliedOn(map);
		offer.setPercentage(10d);
		offer.setDescription("Oranges 10% off");
		List<SpecialOffer> specialOffers = new ArrayList<>();
		specialOffers.add(offer);
		specialOfferResponse.setData(specialOffers);
	}

}
