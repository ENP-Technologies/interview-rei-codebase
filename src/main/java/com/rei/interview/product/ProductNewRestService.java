package com.rei.interview.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.rei.interview.checkout.CartService;
import com.rei.interview.rs.product.ProductDto;

@RestController
@RequestMapping("/products")
public class ProductNewRestService {
	private static final Logger logger = LoggerFactory.getLogger(ProductNewRestService.class);
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductService productService;
	
	public ProductNewRestService() {
    }

	
	public void addProduct(List<Product> productList){
		
	
		try {
			
			System.out.println("Modified fromPooja");
			
//			URL csvFile = getClass().getClassLoader().getResource("products2.csv");
//			String csvFilePath = csvFile.toString().replaceAll("%20", " ").replace("file:/", "").replace("/","\\");
//			System.out.println(csvFilePath);
		
		FileWriter writer = new FileWriter("C:\\Users\\pooja\\OneDrive\\Documents\\STS-3 Application\\interview-exercise-PoojaENP-master\\src\\main\\resources\\products.csv",true);
		
		ColumnPositionMappingStrategy mappingStrategy=  new ColumnPositionMappingStrategy();
		mappingStrategy.setType(Product.class);
		
		 // Arrange column name as provided in below array.
        String[] columns = new String[] { "ProductId", "Brand", "Description", "Price" };
        mappingStrategy.setColumnMapping(columns);
        
        // Creating StatefulBeanToCsv object
        StatefulBeanToCsvBuilder<Product> builder= new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv beanWriter =  builder.withMappingStrategy(mappingStrategy).build();
        
     // Write list to StatefulBeanToCsv object
        beanWriter.write(productList);
     // closing the writer object
        writer.close();

		}
		catch(IOException|CsvDataTypeMismatchException|CsvRequiredFieldEmptyException e){
			 System.out.println("Exception Thrown: "
                     + e);
		}
        
        System.out.println("Data entered");
		}
	
	
	@GetMapping(path="/productId/{productId}")
		public ResponseEntity<String> getProductByProductId(@PathVariable("productId") String productId) {
		
		Optional<Product> product = Optional.ofNullable(productService.getProduct(productId));
		
		if(product.isPresent()) {
		String productDetails = "Product Details for ProductId: "+ productId+"<br> Product brand is: "+product.get().getBrand()+"<br>"+" Product description: "+product.get().getDescription()+"<br> Product Price: "+product.get().getPrice(); 
		
		return new ResponseEntity<>(productDetails, HttpStatus.OK);
		}
		
		else {
			String productDetails="Product "+ productId+" Not Found";
			return new ResponseEntity<>(productDetails,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path="/brandName/{brand}")
	public ResponseEntity<List<Product>> getProductByBrand(@PathVariable("brand") String brand) {
	
	Optional<List<Product>> productList = Optional.ofNullable(productService.getProductByBrand(brand));
	
	if(productList.isPresent()) {
//	String productDetails = "Product Details for ProductId: "+ productId+"<br> Product brand is: "+product.get().getBrand()+"<br>"+" Product description: "+product.get().getDescription()+"<br> Product Price: "+product.get().getPrice(); 
	
	return new ResponseEntity<>(productList.get(), HttpStatus.OK);
	}
	
	else {
		//String productDetails="Product "+ productId+" Not Found";
		return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}
}
		
	

}

