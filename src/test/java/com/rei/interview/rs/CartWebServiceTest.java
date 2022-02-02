package com.rei.interview.rs;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.rei.interview.checkout.Cart;
import com.rei.interview.checkout.CartRepository;
import com.rei.interview.checkout.CartService;
import com.rei.interview.inventory.InventoryService;
import com.rei.interview.location.LocationService;
import com.rei.interview.product.Product;
import com.rei.interview.product.ProductRepository;
import com.rei.interview.product.ProductService;
import com.rei.interview.rs.cart.AddItem;
import com.rei.interview.rs.cart.CartDto;
import org.junit.jupiter.api.*;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartWebServiceTest {
	

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
	
    @Test
    public void shouldReturn200WhenSendingRequestCart() throws Exception {
        AddItem addItem = new AddItem();
        addItem.setProductId("123456");
        addItem.setQuantity(1);
        addItem.setLocation(Location.SEATTLE);

        ResponseEntity<CartDto> entity = this.testRestTemplate.postForEntity(
                "http://localhost:"+port+"/cart/123456", addItem, CartDto.class);

        Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
    
    
    @Test
    @DisplayName("Inventory Availability Check for Online and Nearby Stores")
	public void checkInventoryOnlineAndStoreAvailability() throws Exception{
	InventoryService inventoryService = new InventoryService(new LocationService());
	Product product = new Product();
	product.setProductId("12345");
	product.setBrand("Brand1");
	product.setPrice(new BigDecimal("100.00"));
	product.setDescription("New Test Product");
	boolean isInventoryOnlineAvailable = inventoryService.hasInventoryOnline(product, 0);
	System.out.println("OnlineAvailablity "+isInventoryOnlineAvailable);
	Assert.assertFalse("Assert false if quantity is less than 1 to show not available",isInventoryOnlineAvailable);
	
	
	boolean isInventoryStoreAvailable = inventoryService.hasInventoryInNearbyStores(product, 2, Location.SEATTLE);
	System.out.println("InventoryStoreAvailablity "+isInventoryStoreAvailable);
	Assert.assertTrue("Assert true if the quantity is greater than 1 in nearby stores", isInventoryStoreAvailable);
	   	     	
}
    

//    
//    @Test
//    public void checkInventoryUpdatedAtEveryScheduledInterval() throws Exception{
//    	ProductRepository productRepository = new ProductRepository();
//    	ProductService productService = new ProductService(productRepository);
//    	InventoryService inventoryService = new InventoryService(new LocationService());
//    	CartRepository cartRepository = new CartRepository();
//    	
//    	Product product1 = new Product();
//    	product1.setProductId("12345");
//    	product1.setBrand("Brand1");
//    	product1.setPrice(new BigDecimal("100.00"));
//    	product1.setDescription("New Test Product");
//    	CartService cartService = new CartService(productService, inventoryService, cartRepository);
//    	
//    	Cart cart = cartService.addToCart("12345", product1, 0, Location.GREENVILLE);
//    	
    	
    //}
    
    
    @SpyBean
    private InventoryService inventoryService;

    @Test
    public void scheduledJobRunsEvery5Minutes() {
    	 Awaitility.await().atMost(5, TimeUnit.MINUTES).untilAsserted(() ->
         verify(inventoryService, Mockito.atLeastOnce()).readInInventory());
    }
    
}
