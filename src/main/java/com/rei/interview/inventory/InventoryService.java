package com.rei.interview.inventory;

import com.rei.interview.location.LocationService;
import com.rei.interview.product.Product;
import com.rei.interview.rs.Location;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class InventoryService {

    private LocationService locationService;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    public InventoryService(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostConstruct
    //Refreshing data from inventory every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void readInInventory() throws IOException {
    	System.out.println("Scheduling at interval "+dateFormat.format(new Date()));
        try(Reader in = new InputStreamReader(getClass().getResourceAsStream("/product_inventory.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader("productId", "location", "quantity")
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                String productId = record.get("productId");
                String location = record.get("location");
                int quantity = Integer.valueOf(record.get("quantity"));
                System.out.println(productId + "\t" + location + "\t" + quantity);
            }
        }
    }

    public boolean hasInventoryOnline(Product product, int quantity) {
    	if(quantity>0) {
    		return true;
    	}
    	else return false;
    }

    public boolean hasInventoryInNearbyStores(Product product, int quantity, Location currentLocation) {
        if(quantity<=1) {
    	return false;
        }
        else return true;
    }
}
