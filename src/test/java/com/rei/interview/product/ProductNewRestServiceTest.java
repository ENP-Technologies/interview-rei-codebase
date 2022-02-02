package com.rei.interview.product;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductNewRestServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
	
	@Test
	 @DisplayName("Check if method AddProduct added successfully Product to the Product list")
	void verifySuccessfulWhenAddedProductstoProductsCSV() throws Exception{
		ProductNewRestService productNewRestService = mock(ProductNewRestService.class);
		List<Product> productList = new ArrayList<Product>();
		Product prod1 = new Product("id1","brand1","description1",new BigDecimal("1"));
		Product prod2 = new Product("id2","brand2","description2",new BigDecimal("2"));
		Product prod3 = new Product("id3","brand3","description3",new BigDecimal("2"));
		
		productList.add(prod1);
		productList.add(prod2);
		productList.add(prod3);
		
			
		doCallRealMethod().when(productNewRestService).addProduct(anyList());
		productNewRestService.addProduct(productList);
		
		verify(productNewRestService,times(1)).addProduct(productList);
	
	}
	
	
	@Test
	 @DisplayName("Return Product for provided productId")
	void shouldReturn200WhenGettingProductByProductIdElseFailTest() throws Exception{
	ProductNewRestService productNewRestService = new ProductNewRestService();
	
	ResponseEntity<String> response= this.testRestTemplate.getForEntity("http://localhost:"+port+"/products/productId/123456",String.class);
	
	assertEquals(HttpStatus.OK, response.getStatusCode(),"Product Id not found");
	}
	
	@Test
	 @DisplayName("Return Product for provided productId")
	void shouldReturn200WhenGettingProductByBrandElseFailTest() throws Exception{
	ProductNewRestService productNewRestService = new ProductNewRestService();
	
	ResponseEntity<String> response= this.testRestTemplate.getForEntity("http://localhost:"+port+"/products//brandName/Patagonia",String.class);
	
	assertEquals(HttpStatus.OK, response.getStatusCode(),"Products related to Brand not found");
	}

}
