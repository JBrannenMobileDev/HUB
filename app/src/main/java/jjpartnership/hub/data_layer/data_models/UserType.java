package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/23/18.
 */

public class UserType {
    public static final String SALES_AGENT = "sales_agent";
    public static final String CUSTOMER = "customer";

    public static String getTypeId(boolean salesAgentSelected) {
        if(salesAgentSelected){
            return SALES_AGENT;
        }else{
            return CUSTOMER;
        }
    }
}
