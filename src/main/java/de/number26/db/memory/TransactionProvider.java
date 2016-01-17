package de.number26.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.number26.resource.entity.Transaction;

/**
 * Database in memory modeled as a hash table.
 * 
 * @author fcojavierob
 */
public class TransactionProvider {
	
	// Singleton
	private static final TransactionProvider instance = new TransactionProvider();
	 
	protected TransactionProvider() {}
 
	// Runtime initialization. By default ThreadSafe
	public static TransactionProvider getInstance() {
		return instance;
	}

	// <Key, Value> = <TransactionId, Transaction>
    public static Map<Long, Transaction> transactionStorage = new HashMap<Long, Transaction>();
    
    /**
     * Evaluate if there a transaction whose id is transactionId.
     * 
     * @param transactionId Number greater than zero that identifies a transaction in transactionStorage.
     * @return result of the evaluation.
     * */
    public boolean hasTransactionId(Long transactionId) {
        return transactionStorage.containsKey(transactionId);
    }
    
    /**
     * Retrieve a transaction whose id is transactionId. Otherwise returns null.
     * 
     * @param transactionId Number greater than zero that identifies a transaction in transactionStorage.
     * @return requested transaction.
     * */
    public Transaction getTransaction(Long transactionId) {
        return transactionStorage.get(transactionId);
    }
    
    /**
     * If transactionId exists, update its content with transaction otherwise creates a new 
     * transaction.
     * 
     * @param transactionId Number greater than zero that identifies a transaction in transactionStorage.
     * @param transaction valid transaction content to update transactionId.
     * @return the updated transaction.
     * */
    public Transaction updateTransaction(Long transactionId, Transaction transaction) {
    	// If old value was already created the old value is replaced,
    	// otherwise the new value is inserted.
    	transaction.setTransactionId(transactionId);
    	transactionStorage.put(transactionId, transaction);
    	return transaction;
    }

    /**
     * Compute the sum of all the transaction that are linked to transactionId.
     * 
     * @param transactionId Number greater than zero that identifies a transaction in transactionStorage.
     * @return total sum 
     * */
    public Double getTransactionSum(Long transactionId) {
        Transaction transaction = transactionStorage.get(transactionId);
        Double sum = transaction.getAmount();
        while (transaction.getParentId() > 0) {
            transaction = transactionStorage.get(transaction.getParentId());
            sum += transaction.getAmount();
        }
        return sum;
    }
    
    /**
     * Compute all the transaction whose Transaction.type is type.
     * 
     * @param  type field that labels a set of transaction.
     * @return list of transaction id.
     * */
    public List<Long> getTransactionIds(String type){
        List<Transaction> transactions = new ArrayList<Transaction>(transactionStorage.values());
        List<Long> transactionIds = new ArrayList<Long>();
        for(Transaction transaction : transactions)  {
            if(transaction.getType().equalsIgnoreCase(type)) {
                transactionIds.add(transaction.getTransactionId());
            }
        }
        return transactionIds;
    }
    
    /**
     * Remove all the content in transactionStorage. Added for testing purposes.
     * */
    public void clear() {
        transactionStorage.clear();
    }
}
