package de.number26.resource.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.number26.resource.Utils;

/**
 * Custom entity provider that wraps the sum of all transactions that are
 * transitively linked by their parent_id to $transaction_id.
 * 
 *  @author fcojavierob
 */
@XmlRootElement(name = "sum")
public class Sum {
	
	 @XmlElement(name=Utils.SUM_PARAM_NAME)
    private double sum;

    public Sum() {
    	super();
    }
    
    public Sum(double sum) {
    	this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if((obj == null) || (obj.getClass() != this.getClass())) {
    		return false; 
    	}
        final Sum other = (Sum) obj;
        if (this.sum != other.sum) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sum : " + this.sum);
        return sb.toString();
    }
}
