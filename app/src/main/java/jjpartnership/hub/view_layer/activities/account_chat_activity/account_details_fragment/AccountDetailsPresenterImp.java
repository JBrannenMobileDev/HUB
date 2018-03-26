package jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;

/**
 * Created by Jonathan on 3/26/2018.
 */

public class AccountDetailsPresenterImp implements AccountDetailsPresenter {
    private AccountDetailsView fragment;
    private Realm realm;
    private AccountRealm account;
    private CompanyRealm company;
    private List<UserRealm> customers;
    private GroupChatRealm chat;
    private List<UserRealm> salesAgents;
    private String accountName;
    private String accountId;

    public AccountDetailsPresenterImp(AccountDetailsView fragment, String account_name, String account_id) {
        this.fragment = fragment;
        this.realm = RealmUISingleton.getInstance().getRealmInstance();
        this.accountName = account_name;
        this.accountId = account_id;
        customers = new ArrayList<>();
        salesAgents = new ArrayList<>();
        fetchData();
    }

    private void fetchData() {
        account = realm.where(AccountRealm.class).equalTo("accountId", accountId).findFirst();
        if(account != null) {
            company = realm.where(CompanyRealm.class).equalTo("companyId", account.getCompanyCustomerId()).findFirst();
            if(company != null) {
                parseDetailsData(company);
            }
            List<String> employeeList = getCompanyEmployeeUids(company.getEmployeeList());
            if(employeeList.size() > 0){
                for(String uid : employeeList){
                    UserRealm customer = realm.where(UserRealm.class).equalTo("uid", uid).findFirst();
                    if(customer != null) customers.add(customer);
                }
            }
            if (customers.size() > 0) {
                fragment.onRecieveCustomers(customers);
            }
            chat = realm.where(GroupChatRealm.class).equalTo("chatId", account.getGroupChatSalesId()).findFirst();
            if(chat != null){
                if(chat.getUserIds() != null && chat.getUserIds().size() > 0){
                    for(String uid : chat.getUserIds()){
                        UserRealm salesAgent = realm.where(UserRealm.class).equalTo("uid", uid).findFirst();
                        if(salesAgent != null)salesAgents.add(salesAgent);
                    }
                }
            }
            if(salesAgents.size() > 0){
                fragment.onReceiveSalesAgentData(salesAgents);
            }
        }
    }

    private List<String> getCompanyEmployeeUids(RealmList<String> employeeList) {
        if(employeeList != null && employeeList.size() > 0){
            return employeeList;
        }else{
            return new ArrayList<>();
        }
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

    }
}
