package de.number26.test;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.number26.Main;
import de.number26.resource.Utils;
import de.number26.resource.entity.Transaction;

/**
 * Test for resources:
 * 		GET transactionservice/transaction/{transaction_id}
 * 		PUT transactionservice/transaction/{transaction_id}
 * 
 * @author fcojavierob
 */
public class TransactionResourceTest {
	
	private HttpServer server;
	private Client client;
	private static final String TRANSACTION_RESOURCE_URI_TEST = Main.BASE_URI + Utils.TRANSACTION_RESOURCE_PATH;
	
    @Before
    public void setUp() throws Exception {
        final ResourceConfig rc = Main.createApp();
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Main.BASE_URI), rc);     
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
        client.close();
    }
    
    /**
     * Get - Create a new transaction and request it
     **/
    @Test
    public void testGetTransactionSuccess() {
    	final long transactionId = 2;
    	final Transaction newTransaction = new Transaction(transactionId, 25000, "shopping", -1);
        // First create a new transaction
    	final String resourceUri = TRANSACTION_RESOURCE_URI_TEST + String.valueOf(transactionId);
    	final Transaction returnedTransaction = client.target(resourceUri)
        						.request(MediaType.APPLICATION_JSON_TYPE)
        						.put(Entity.entity(newTransaction, MediaType.APPLICATION_JSON_TYPE), Transaction.class);
        assertEquals(newTransaction, returnedTransaction);
        
        // Once created successfully, use get to request the value again
        final Transaction testTransaction = client.target(resourceUri)
        						.request(MediaType.APPLICATION_JSON_TYPE)
        						.get(Transaction.class);
        assertEquals(newTransaction, testTransaction);
    }    
  
    /**
     * Get - No {transaction_id} parameter
     **/
    @Test
    public void testGetTransactionNoId() {
    	final String resourceUri = TRANSACTION_RESOURCE_URI_TEST; // No transaction id
        final Response result = client.target(resourceUri).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, result.getStatus());
    }
    
    /**
     * Get - No numeric {transaction_id} parameter
     **/
    @Test
    public void testGetTransactionNoNumericId() {
    	final String resourceUri = TRANSACTION_RESOURCE_URI_TEST + "NoNumericId"; // No numeric id
        final Response result = client.target(resourceUri).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, result.getStatus());
    }
    
    /**
     * Get - Non existent {transaction_id} parameter
     **/
    @Test
    public void testGetTransactionNonExistent() {
    	final long transactionId = 10100;
    	final String resourceUri = TRANSACTION_RESOURCE_URI_TEST + String.valueOf(transactionId);
        final WebTarget target = client.target(resourceUri);
        final Response result = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(400, result.getStatus());
    }

    /**
     * Put - Create 2 transaction related with each other
     **/
    @Test
    public void testPutTransactionSuccess() {
    	// Transaction:
    	// {"amount": 5000, "type": "cars", "parentId": -1}
    	final long transactionIdCars = 10;
        final Transaction newTransactionCars = new Transaction(transactionIdCars, 5000, "cars", -1);
        final String resourceUriCars = TRANSACTION_RESOURCE_URI_TEST + String.valueOf(transactionIdCars);
        final Transaction returnedTransactionCars = client.target(resourceUriCars)
        												.request(MediaType.APPLICATION_JSON_TYPE)
        												.put(Entity.entity(newTransactionCars, 
        														MediaType.APPLICATION_JSON_TYPE), Transaction.class);
        assertEquals(returnedTransactionCars, newTransactionCars);
        
    	// Transaction related with the first one
    	// {"amount": 5000, "type": "cars", "parentId": 10}
        final long transactionIdShopping = 11;
        final Transaction newTransactionShopping = new Transaction(transactionIdShopping, 10000, "shopping", transactionIdCars);
        final String resourceUriShopping = TRANSACTION_RESOURCE_URI_TEST + String.valueOf(transactionIdShopping);
        final Transaction returnedTransactionShopping = client.target(resourceUriShopping)
    													.request(MediaType.APPLICATION_JSON_TYPE)
    													.put(Entity.entity(newTransactionShopping, 
    															MediaType.APPLICATION_JSON_TYPE), Transaction.class);
        assertEquals(returnedTransactionShopping, newTransactionShopping);
    }
    
    /**
     * Put - No transaction id specified
     **/
    @Test
    public void testPutTransactionNoId() throws Exception {
    	// Put - no transaction id
        final Transaction transactionPutNoId = new Transaction(-1, 5000, "shopping", -1);
        final String resourceUri = TRANSACTION_RESOURCE_URI_TEST; // no id
        Response result = client.target(resourceUri)
								.request(MediaType.APPLICATION_JSON_TYPE)
								.put(Entity.entity(transactionPutNoId, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(404, result.getStatus());
    }
    
    /**
     * Put - Transaction id no numeric
     **/
    @Test
    public void testPutTransactionNoNumericId() throws Exception {
        // Put - transaction no numeric id  
        final Transaction transactionNoValidId = new Transaction(-1, 5000, "shopping", -1);
    	final String resourceUri = TRANSACTION_RESOURCE_URI_TEST + "NoNumericId"; // No numeric id
        final Response result = client.target(resourceUri)
								.request(MediaType.APPLICATION_JSON_TYPE)
								.put(Entity.entity(transactionNoValidId, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(404, result.getStatus());
    }
    
    /**
     * Put - Update an previously created transaction. Repeat the same request twice with different values.
     **/
    @Test
    public void testPutAlreadyExistTransaction() throws Exception {
    	final long transactionId = 333333;
        final WebTarget target = client.target(TRANSACTION_RESOURCE_URI_TEST + String.valueOf(transactionId));
        final Transaction newTransactionFirst = new Transaction(transactionId, 15000, "shopping", -1);
 
        final Transaction returnedTransactionFirst  = target
        						.request(MediaType.APPLICATION_JSON_TYPE)
        						.put(Entity.entity(newTransactionFirst, MediaType.APPLICATION_JSON_TYPE), Transaction.class);
        assertEquals(returnedTransactionFirst, newTransactionFirst);
        
        final Transaction newTransactionSecond = new Transaction(transactionId, 15000, "shopping", -1);
        final Transaction returnedTransactionSecond = target
        						.request(MediaType.APPLICATION_JSON_TYPE)
        						.put(Entity.entity(newTransactionSecond, MediaType.APPLICATION_JSON_TYPE), Transaction.class);
        assertEquals(returnedTransactionSecond, newTransactionSecond);
    }
   
}
