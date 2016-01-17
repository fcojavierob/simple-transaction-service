package de.number26.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.number26.db.memory.TransactionProvider;
import de.number26.resource.entity.Transaction;
import de.number26.resource.validation.InputValidator;
import de.number26.resource.Utils;

/**
 *  Resources for /transaction request.
 *  
 * @author fcojavierob
 */
@Path(Utils.TRANSACTION_RESOURCE_PATH)
public class TransactionResource {
	
	// Simulated Database in memory as a HashMap
    private TransactionProvider transactionProvider = TransactionProvider.getInstance();

    /**
     * GET http://localhost:8080/transactionservice/transaction/{transaction_id}
     */
    @GET
    @Path(Utils.TRANSACTION_ID_PARAM_NAME_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransaction(@PathParam(Utils.TRANSACTION_ID_PARAM_NAME) long transactionId) {
    	// Non negative or zero transaction id values
    	// Transaction must exist in TransactionProvider
    	InputValidator.validateTransactionId(transactionId);
        try {
        	Transaction transaction = transactionProvider.getTransaction(transactionId);
            return Response.ok(transaction).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     *  First check if transaction and transactionId are valid values.
     *  If transactionId already exists, update it otherwise create it.
     * 
     *  PUT http://localhost:8080/transactionservice/transaction/{transaction_id}
     */
    @PUT
    @Path(Utils.TRANSACTION_ID_PARAM_NAME_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTransaction(@PathParam(Utils.TRANSACTION_ID_PARAM_NAME) long transactionId, Transaction transaction) {
    	// Non null transaction.type
    	// Non negative transaction.amount
    	InputValidator.validateTransaction(transaction);
    	try {
            Transaction transactionUpdated = transactionProvider.updateTransaction(transactionId, transaction);
            return Response.ok(transactionUpdated).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
