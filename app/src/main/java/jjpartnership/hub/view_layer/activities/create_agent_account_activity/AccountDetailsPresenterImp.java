package jjpartnership.hub.view_layer.activities.create_agent_account_activity;

import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.utils.StringValidationUtil;

/**
 * Created by jbrannen on 3/2/18.
 */

public class AccountDetailsPresenterImp implements AccountDetailsPresenter {
    private static final String FIRST_NAME_ERROR = "Please provide your first name.";
    private static final String LAST_NAME_ERROR = "Please provide your last name.";
    private static final String PHONE_NUMBER_ERROR = "Please provide your phone number.";
    private static final String PHONE_NUMBER_VALIDATION_ERROR = "Please provide a valid phone number.";
    private static final String BUSINESS_UNIT_ERROR = "Please provide your business unit.";
    private static final String ROLE_ERROR = "Please provide your role.";
    private AccountDetailsView activity;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String businessUnit;
    private String role;
    private FirebaseAuth mAuth;

    public AccountDetailsPresenterImp(AccountDetailsView activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        fetchCompanyName();
    }

    private void fetchCompanyName() {
//        UserCompanyName companyName = Realm.getDefaultInstance().where(UserCompanyName.class).findFirst();
//        companyName.addChangeListener(new RealmChangeListener<UserCompanyName>() {
//            @Override
//            public void onChange(UserCompanyName name) {
//                activity.setCompanyName(name.getUserCompanyName());
//            }
//        });
//        activity.setCompanyName(companyName.getUserCompanyName());
    }

    @Override
    public void onNextClicked() {
        if(firstName != null && firstName.length() > 0){
            if(lastName != null && lastName.length() > 0){
                if(phoneNumber != null && phoneNumber.length() > 0){
                    if(StringValidationUtil.isValidPhoneNumber(phoneNumber)){
                        activity.onNextView();
                    }else{
                        activity.onUserInputError(PHONE_NUMBER_VALIDATION_ERROR);
                    }
                }else{
                    activity.onUserInputError(PHONE_NUMBER_ERROR);
                }
            }else{
                activity.onUserInputError(LAST_NAME_ERROR);
            }
        }else{
            activity.onUserInputError(FIRST_NAME_ERROR);
        }
    }

    @Override
    public void onSubmitClicked() {
        if(businessUnit != null && businessUnit.length() > 0){
            if(role != null && role.length() > 0){
                onFormComplete();
            }else{
                activity.onUserInputError(ROLE_ERROR);
            }
        }else{
            activity.onUserInputError(BUSINESS_UNIT_ERROR);
        }
    }

    private void onFormComplete() {
        DataManager.getInstance().updateUser(firstName, lastName, phoneNumber, businessUnit, role);
        activity.onFormComplete();
    }

    @Override
    public void onFirstNameUpdated(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void onLastNameUpdated(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void onPhoneNumberUpdated(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onBusinessUnitUpdated(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public void onRoleUpdated(String role) {
        this.role = role;
    }
}
