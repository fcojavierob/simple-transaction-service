package de.number26.resource.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.number26.resource.Utils;

/**
 *  Custom entity provider for transaction objects. 
 * 	transactionId: is a long specifying a new transaction
 *	amount: is a double specifying the amount
 *	type: is a string specifying a type of the transaction.
 *	parent_id: is an optional long that may specify the parent transaction of this transaction.
 *
 *  @author fcojavierob
 */
@XmlRootElement(name = "Transaction")
public class Transaction {
	
    @XmlElement(name= Utils.TRANSACTION_ID_PARAM_NAME)
    private long transactionId;
    
    @XmlElement(name= Utils.AMOUNT_PARAM_NAME)
    private double amount;
    
    @XmlElement(name= Utils.TYPE_PARAM_NAME)
    private String type;
    
    @XmlElement(name=Utils.PARENT_ID_PARAM_NAME)
    private long parentId;

    public Transaction() {
    	this.transactionId = -1;
    	this.amount = -1;
    	this.type = "";
    	this.parentId = -1;
    }
    
    public Transaction(long transactionId, double amount, String type, long parentId) {
    	this.transactionId = transactionId;
    	this.amount = amount;
    	this.type = type;
    	this.parentId = parentId;
    }
    
    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if((obj == null) || (obj.getClass() != this.getClass())) {
    		return false; 
    	}
        final Transaction other = (Transaction) obj;
        if (this.transactionId != other.transactionId) {
            return false;
        }
        if (this.amount != other.amount) {
            return false;
        }
        if (this.parentId != other.parentId) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("transactionId : " + this.transactionId + ",");
        sb.append("amount : " + this.amount + ",");
        sb.append("type : " + this.type + ",");
        sb.append("parentId : " + this.parentId);
        return sb.toString();
    }
    
}
