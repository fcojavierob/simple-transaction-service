package de.number26.resource;

/**
 * Class to group all the Jersey constant values to specify resource paths and parameters.
 * 
 * @author fcojavierob
 */
public class Utils {
	
	// Resource Path constants
    public static final String TRANSACTION_RESOURCE_PATH = "transaction/";
    public static final String SUM_RESOURCE_PATH = "sum/";
    public static final String TYPE_RESOURCE_PATH = "types/";
    
    // Parameter constants
    public static final String AMOUNT_PARAM_NAME = "amount";
    public static final String PARENT_ID_PARAM_NAME = "parent_id";
    public static final String TRANSACTION_ID_PARAM_NAME = "transaction_id";
    public static final String TYPE_PARAM_NAME = "type";
    public static final String SUM_PARAM_NAME = "sum";
    
    // Parameter Path constants
    public static final String TYPE_PARAM_NAME_PATH = "/{" + TYPE_PARAM_NAME + "}";
    public static final String TRANSACTION_ID_PARAM_NAME_PATH = "/{" + TRANSACTION_ID_PARAM_NAME + "}";
    
}
