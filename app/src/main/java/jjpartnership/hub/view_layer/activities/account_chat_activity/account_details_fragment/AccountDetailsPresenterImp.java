package jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.CustomerRequest;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/26/2018.
 */

public class AccountDetailsPresenterImp implements AccountDetailsPresenter {
    private AccountDetailsView fragment;
    private Realm realm;
    private AccountRealm account;
    private CompanyRealm company;
    private GroupChatRealm chat;
    private List<UserRealm> salesAgents;
    private List<UserRealm> checkedUsers;
    private String accountName;
    private String accountId;

    public AccountDetailsPresenterImp(AccountDetailsView fragment, String account_name, String account_id) {
        this.fragment = fragment;
        this.realm = RealmUISingleton.getInstance().getRealmInstance();
        this.accountName = account_name;
        this.accountId = account_id;
        salesAgents = new ArrayList<>();
        checkedUsers = new ArrayList<>();
        fetchData();
    }

    private void fetchData() {
        account = realm.where(AccountRealm.class).equalTo("accountIdFire", accountId).findFirst();
        if(account != null) {
            company = realm.where(CompanyRealm.class).equalTo("companyId", account.getCompanyCustomerId()).findFirst();
            if(company != null) {
                parseDetailsData(company);
            }

            if(account.getAccountSalesAgentUids() != null && account.getAccountSalesAgentUids().size() > 0){
                for(String uid : account.getAccountSalesAgentUids()){
                    if(!uid.equals(UserPreferences.getInstance().getUid())) {
                        UserRealm salesAgent = realm.where(UserRealm.class).equalTo("uid", uid).findFirst();
                        if (salesAgent != null) salesAgents.add(salesAgent);
                    }
                }
            }

            if(salesAgents.size() > 0){
                Collections.sort(salesAgents);
                fragment.onReceiveSalesAgentData(salesAgents);
            }
        }

//        createNewRequest();
    }

    private void createNewRequest() {
        DataManager.getInstance().createNewCustomerRequest(account, company, "Can anyone provide a custome chemical capsul?", company.getEmployeeList().get(0));
    }

    private void parseDetailsData(CompanyRealm company) {
        String address = "";
        String industries = "";
        if(company.getAddress() != null && !company.getAddress().isEmpty()){
            address = company.getAddress();
        }
        if(company.getIndustryList() != null && company.getIndustryList().size() > 0){
            for(String industry : company.getIndustryList()){
                if(industries.isEmpty()) {
                    industries = industry;
                }else{
                    industries = industry + ", " + industry;
                }
            }
        }
        fragment.onReceiveCompanyData(address, industries);
    }

    @Override
    public void onDirectionsClicked() {
        fragment.launchDirectionsIntent(company.getAddress());
    }

    @Override
    public void onCheckboxClicked(UserRealm user, Boolean checked) {
        if(checked) {
            fragment.setNewGroupTextEnabled();
            checkedUsers.add(user);
        }else{
            checkedUsers.remove(user);
            if(checkedUsers.size() == 0)fragment.setNewGroupTextDissabled();
        }
    }

    @Override
    public void onNewGroupChatClicked() {
        if(checkedUsers.size() > 0){
            fragment.showNewGroupDialog(getAgentIds(salesAgents), getAgentIds(checkedUsers));
        }else{
            fragment.showNoAgentsSelectedToast();
        }
    }

    private ArrayList<String> getAgentIds(List<UserRealm> salesAgents) {
        ArrayList<String> ids = new ArrayList<>();
        for(UserRealm user : salesAgents){
            ids.add(user.getUid());
        }
        return ids;
    }
}
