package de.number26.resource.validation;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import de.number26.db.memory.TransactionProvider;
import de.number26.resource.entity.Transaction;;

/**
 * Provides a collection of predicates to validate RESTful service input parameters.
 * 
 * @author fcojavierob
 */
public class InputValidator {
  private static TransactionProvider transactionProvider = TransactionProvider.getInstance();
  
  public static void validateTransactionId(Long transactionId) {
	  if (!isValidTransactionId(transactionId)) {
	  		throw new WebApplicationException(Status.BAD_REQUEST);
	  }   
  }
  
  public static void validateString(String value) {
	  if (isNullOrEmpty(value)) {
		  throw new WebApplicationException(Status.BAD_REQUEST);
	  }
  }
  
  public static void validateTransaction(Transaction transaction) {
	 if (!isValidTransaction(transaction)) {
		  throw new WebApplicationException(Status.BAD_REQUEST);
	  }
  }
  
  private static boolean isValidTransactionId(Long transactionId) {
	  return (transactionId > 0 && transactionProvider.hasTransactionId(transactionId));
  }
  
  private static boolean isNullOrEmpty(String value) {
	  return (value == null || "".equals(value));
  }
  
  private static boolean isValidTransaction(Transaction transaction) {
	  return (transaction != null && 
			  transaction.getAmount() >= 0 && 
			  !isNullOrEmpty(transaction.getType()));
  }
  
  public static InputValidator getInstance() {
    return new InputValidator();
  }
}
