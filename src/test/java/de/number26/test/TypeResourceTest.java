package de.number26.test;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.number26.Main;
import de.number26.db.memory.TransactionProvider;
import de.number26.resource.Utils;
import de.number26.resource.entity.Transaction;

/**
 * Test for GET transactionservice/types/{typeName}
 * 
 * @author fcojavierob
 */
public class TypeResourceTest {
	
	private HttpServer server;
	private Client client;
	private static final String TYPE_RESOURCE_URI_TEST = Main.BASE_URI + Utils.TYPE_RESOURCE_PATH;
	
    @Before
    public void setUp() throws Exception {
        final ResourceConfig rc = Main.createApp();
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(Main.BASE_URI), rc);     
        client = ClientBuilder.newClient();
        TransactionProvider.getInstance().clear();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
        client.close();
    }
    
    /**
     * Get - Create transaction with new types and request them.
     **/
    @Test
	public void testGetTypesSuccess() {
    	final int numberOfCars = 4;
    	final List<Long> expectedResult = new ArrayList<Long>();
    	final String typeToTest = "cars";
        // First create a new "cars" transaction set
    	for (int i = 1; i <= numberOfCars; ++i) {
    		final Transaction newTransaction = new Transaction(i, 25000, typeToTest, -1);
        	final String resourceUri = Main.BASE_URI + Utils.TRANSACTION_RESOURCE_PATH + String.valueOf(i);
        	final Transaction returnedTransaction = client.target(resourceUri)
            						.request(MediaType.APPLICATION_JSON_TYPE)
            						.put(Entity.entity(newTransaction, MediaType.APPLICATION_JSON_TYPE), Transaction.class);
            assertEquals(newTransaction, returnedTransaction);
    		expectedResult.add(new Long(i));
    	}
    	
    	// Retry the id of the transaction whose type is "cars"
        final WebTarget targetBadPath = client.target(TYPE_RESOURCE_URI_TEST  + typeToTest);
        final List<Long> result = targetBadPath
        							.request(MediaType.APPLICATION_JSON_TYPE)
        							.get(new GenericType<List<Long>>() {});  
        assertEquals(expectedResult, result);
	}
    
    /**
     * Get - Non existent {type} parameter.
     **/
    @Test
	public void testGetTypesNonExistent() {
    	List<Long> emptyList = new ArrayList<Long>();
        final String resourcePathNonExistent = TYPE_RESOURCE_URI_TEST  + "nada";
        final List<Long> result = client.target(resourcePathNonExistent)
        							.request(MediaType.APPLICATION_JSON_TYPE)
        							.get(new GenericType<List<Long>>() {});  
        assertEquals(emptyList, result);
	}
    
    /**
     * Get - Request with no {type} parameter.
     **/
    @Test
	public void testGetTypesNotype() {
        final Response result = client.target(TYPE_RESOURCE_URI_TEST)
        							.request(MediaType.APPLICATION_JSON_TYPE)
        							.get();  
        assertEquals(404, result.getStatus());
        
	}
    
    /**
     * Put - Perform put against /types/{type}
     **/
    @Test
	public void testGetTypesSendPut() {
    	final long transactionId = 1;
    	final String typeToTest = "cars";
		final Transaction newTransaction = new Transaction(transactionId, 25000, typeToTest, -1);
    	final String resourceUri = TYPE_RESOURCE_URI_TEST  + typeToTest;
    	final Response result = client.target(resourceUri)
        						.request(MediaType.APPLICATION_JSON_TYPE)
        						.put(Entity.entity(newTransaction, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(405, result.getStatus());
	}
}