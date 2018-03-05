package jjpartnership.hub.view_layer.activities.create_customer_account_activity;

/**
 * Created by jbrannen on 3/2/18.
 */

public interface CustomerAccountDetailsView {
    void onUserInputError(String s);
    void onNextView();
    void onFormComplete();
    void setCompanyName(String name);
}
