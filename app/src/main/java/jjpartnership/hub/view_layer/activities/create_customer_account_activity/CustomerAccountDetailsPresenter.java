package jjpartnership.hub.view_layer.activities.create_customer_account_activity;

/**
 * Created by jbrannen on 3/2/18.
 */

public interface CustomerAccountDetailsPresenter {
    void onNextClicked();
    void onSubmitClicked();
    void onFirstNameUpdated(String firstName);
    void onLastNameUpdated(String lastName);
    void onPhoneNumberUpdated(String phoneNumber);
    void onBusinessUnitUpdated(String businessUnit);
    void onRoleUpdated(String role);
}
