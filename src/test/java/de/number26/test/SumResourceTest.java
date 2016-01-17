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
import de.number26.resource.entity.Sum;
import de.number26.resource.entity.Transaction;

/**
 * Test for GET transactionservice/sum/{transaction_id}
 * 
 * @author fcojavierob
 */
public class SumResourceTest {
	
	private HttpServer server;
	private Client client;
	private static final String SUM_RESOURCE_URI_TEST = Main.BASE_URI + Utils.SUM_RESOURCE_PATH;
	private static final int NUMBER_OF_CARS = 4; // Number of transactions of type "car" as Setup
	
    @Before
    public void setUp() throws Exception {
        final ResourceConfig rc = Main.createApp();
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Main.BASE_URI), rc);       
        client = ClientBuilder.newClient();
        
    	for (int i = 1; i <= NUMBER_OF_CARS; ++i) {
			final Transaction newTransaction = new Transaction(i, 100, "cars", i - 1); // create link between the transactions
	    	final String resourceUri = Main.BASE_URI + Utils.TRANSACTION_RESOURCE_PATH + String.valueOf(i);
	    	final Transaction returnedTransaction = client.target(resourceUri)
	        						.request(MediaType.APPLICATION_JSON_TYPE)
	        						.put(Entity.entity(newTransaction, MediaType.APPLICATION_JSON_TYPE), Transaction.class);
	        assertEquals(newTransaction, returnedTransaction);
    	}
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
        client.close();
    }
    
    /**
   	 * Get - Retrieve the sum from a transaction with a depth of 1
  	 **/
    @Test
	public void testGetSumZeroDepth() {
    	final long transactionId = 1;  // Depth 0 in setUp
    	final Sum sumZeroDepthExpected = new Sum(100); // Each transaction has an amount of 100
        final Sum sumZeroDepth = client.target(SUM_RESOURCE_URI_TEST + String.valueOf(transactionId))
								.request(MediaType.APPLICATION_JSON_TYPE)
								.get(Sum.class);
        assertEquals(sumZeroDepthExpected, sumZeroDepth);
	}

    /**
   	 * Get - Retrieve the sum from a transaction with a depth of 1
  	 **/
    @Test
	public void testGetSumOneDepth() {
    	final long transactionId = 2;  // Depth 1 in setUp
    	final Sum sumOneDepthExpected = new Sum(200); // Each transaction has an amount of 100
        final Sum sumOneDepth = client.target(SUM_RESOURCE_URI_TEST + String.valueOf(transactionId))
								.request(MediaType.APPLICATION_JSON_TYPE)
								.get(Sum.class);
        assertEquals(sumOneDepthExpected, sumOneDepth);
	}
    
    /**
   	 * Get - Retrieve the sum from a transaction with a depth of 2
  	 **/
    @Test
    public void testGetSumTwoDepth() throws Exception {
    	final long transactionId = 3; // Depth 2 in setUp
    	final Sum sumTwoDepthExpected = new Sum(300); // Each transaction has an amount of 100
        final Sum sumTwoDepth = client.target(SUM_RESOURCE_URI_TEST + String.valueOf(transactionId))
								.request(MediaType.APPLICATION_JSON_TYPE)
								.get(Sum.class);
        assertEquals(sumTwoDepthExpected, sumTwoDepth);
    }
    
    /**
   	 * Get - Non existent {transaction_id} parameter.
  	 **/
    @Test
	public void testGetSumNonExistent() {
        final WebTarget targetNoTransactionId = client.target(SUM_RESOURCE_URI_TEST); // No transaction id
        final Response result = targetNoTransactionId.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, result.getStatus());
	}
    
    /**
     * Get - Request with no {transaction_id} parameter..
  	 **/
    @Test
	public void testGetSumNotype() {
        final WebTarget targetNotValidId = client.target(SUM_RESOURCE_URI_TEST + "/NoValidId"); // no valid id
        final Response result = targetNotValidId.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, result.getStatus());
	}
    
    /**
   	 * Put - Perform put against /sum/{transaction_id}
  	 **/
    @Test
	public void testGetTypesSendPut() {
	 	final long transactionId = 1;
	 	final String typeToTest = "cars";
		final Transaction newTransaction = new Transaction(transactionId, 25000, typeToTest, -1);
	 	final String resourceUri = SUM_RESOURCE_URI_TEST + String.valueOf(transactionId);
	 	final Response result = client.target(resourceUri)
	     						.request(MediaType.APPLICATION_JSON_TYPE)
	     						.put(Entity.entity(newTransaction, MediaType.APPLICATION_JSON_TYPE));
	     assertEquals(405, result.getStatus());
	}
}