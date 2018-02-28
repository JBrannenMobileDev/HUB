package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/24/18.
 */

public class CompanyType {
    public static final String SALES_COMPANY = "sales_company";
    public static final String CUSTOMER_COMPANY = "customer_company";

    public static String getTypeId(boolean salesAgentSelected) {
        if(salesAgentSelected){
            return SALES_COMPANY;
        }else{
            return CUSTOMER_COMPANY;
        }
    }
}
