package de.number26.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.number26.db.memory.TransactionProvider;
import de.number26.resource.validation.InputValidator;

/**
 * Resources for /types request.
 * 
 * @author fcojavierob
 */
@Path(Utils.TYPE_RESOURCE_PATH)
public class TypeResource {

    /**
     * GET http://localhost:8080/transactionservice/types/{type}
     */
	@GET
	@Path(Utils.TYPE_PARAM_NAME_PATH)
	@Produces(MediaType.APPLICATION_JSON)
    public Response getTypes(@PathParam(Utils.TYPE_PARAM_NAME) String type) {
    	// Non null string
    	InputValidator.validateString(type);
        try {
            List<Long> transactionIds = TransactionProvider.getInstance().getTransactionIds(type);
            return Response.ok(transactionIds).build();
        }  catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
