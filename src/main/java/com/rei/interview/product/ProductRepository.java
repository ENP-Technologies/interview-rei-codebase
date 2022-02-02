package com.rei.interview.product;

import com.rei.interview.util.Cache;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

@Repository
public class ProductRepository {

    private Map<String, Product> products = new Cache<>();

    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public Collection<Product> getAll() {
        return products.values();
    }
    
    public List<Product> getProductByBrand(String brand){
    	List<Product> productList = new ArrayList<>();
    	Iterator<Entry<String, Product>> listIterator = products.entrySet().iterator();
 	while(listIterator.hasNext()) {
    	 Map.Entry<String, Product> listElement = (Map.Entry<String, Product>)listIterator.next();
    	 if(listElement.getValue().getBrand().equals(brand)) {
			productList.add((Product) listElement.getValue());
  		}
   }
    	return productList;
   	
    }
}
