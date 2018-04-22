package jjpartnership.hub.data_layer.data_models;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;

/**
 * Created by jbrannen on 3/6/18.
 */

public class Account {
    private String accountIdFire;
    private String accountId;
    private String companySalesId;
    private String companyCustomerId;
    private String groupChatSalesId;
    private Map<String, String> groupChats;
    private Map<String, String> accountUsers;
    private Map<String, String> customerRequestIds;

    public Account() {
    }

    public Account(String accountIdFire, String companyIdA, String companyIdB, String groupChatId,
                   String accountId, Map<String, String> accountUsers, Map<String, String> customerRequestIds, Map<String, String> groupChats) {
        this.accountIdFire = accountIdFire;
        this.companySalesId = companyIdA;
        this.companyCustomerId = companyIdB;
        this.groupChatSalesId = groupChatId;
        this.accountId = accountId;
        this.accountUsers = accountUsers;
        this.customerRequestIds = customerRequestIds;
        this.groupChats = groupChats;
    }

    public Account(AccountRealm accountRealm){
        this.accountIdFire = accountRealm.getAccountIdFire();
        this.companySalesId = accountRealm.getCompanySalesId();
        this.companyCustomerId = accountRealm.getCompanyCustomerId();
        this.groupChatSalesId = accountRealm.getGroupChatSalesId();
        this.accountId = accountRealm.getAccountId();
        this.accountUsers = createMap(accountRealm.getAccountSalesAgentUids());
        this.customerRequestIds = createMapCustomerRequests(accountRealm.getCustomerRequestIds());
        this.groupChats = createMap(accountRealm.getGroupChats());
    }

    private Map<String, String> createMap(RealmList<String> items) {
        Map<String, String> itemsMap = new HashMap<>();
        for(String uid : items){
            itemsMap.put(uid,uid);
        }
        return itemsMap;
    }

    private Map<String, String> createMapCustomerRequests(RealmList<String> customerRequestUids) {
        Map<String, String> requestsMap = new HashMap<>();
        for(String uid : customerRequestUids){
            requestsMap.put(uid,uid);
        }
        return requestsMap;
    }

    public Map<String, String> getGroupChats() {
        return groupChats;
    }

    public void setGroupChats(Map<String, String> groupChats) {
        this.groupChats = groupChats;
    }

    public Map<String, String> getAccountUsers() {
        return accountUsers;
    }

    public void setAccountUsers(Map<String, String> accountUsers) {
        this.accountUsers = accountUsers;
    }

    public Map<String, String> getCustomerRequestIds() {
        return customerRequestIds;
    }

    public void setCustomerRequestIds(Map<String, String> customerRequestIds) {
        this.customerRequestIds = customerRequestIds;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountIdFire() {
        return accountIdFire;
    }

    public void setAccountIdFire(String accountIdFire) {
        this.accountIdFire = accountIdFire;
    }

    public String getCompanySalesId() {
        return companySalesId;
    }

    public void setCompanySalesId(String companySalesId) {
        this.companySalesId = companySalesId;
    }

    public String getCompanyCustomerId() {
        return companyCustomerId;
    }

    public void setCompanyCustomerId(String companyCustomerId) {
        this.companyCustomerId = companyCustomerId;
    }

    public String getGroupChatSalesId() {
        return groupChatSalesId;
    }

    public void setGroupChatSalesId(String groupChatSalesId) {
        this.groupChatSalesId = groupChatSalesId;
    }
}
