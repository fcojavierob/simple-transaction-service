package de.number26.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.number26.db.memory.TransactionProvider;
import de.number26.resource.entity.Sum;
import de.number26.resource.validation.InputValidator;
import de.number26.resource.Utils;

/**
 * Resources for /sum request.
 * 
 * @author fcojavierob
 */
@Path(Utils.SUM_RESOURCE_PATH)
public class SumResource {

    private TransactionProvider transactionProvider = TransactionProvider.getInstance();

    /**
     * GET http://localhost:8080/transactionservice/sum/{transaction_id}
     */
    @GET
    @Path(Utils.TRANSACTION_ID_PARAM_NAME_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSum(@PathParam(Utils.TRANSACTION_ID_PARAM_NAME) long transactionId) {
    	// Non negative or zero transaction id values
    	// Transaction must exist in TransactionProvider
    	InputValidator.validateTransactionId(transactionId);
        try {
            double totalSum = transactionProvider.getTransactionSum(transactionId);
            Sum sum = new Sum(totalSum);
            return Response.ok(sum).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
