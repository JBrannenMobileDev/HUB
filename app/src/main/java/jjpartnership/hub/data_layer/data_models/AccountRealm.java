package jjpartnership.hub.data_layer.data_models;

import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class AccountRealm extends RealmObject{
    @PrimaryKey
    private String accountIdFire;
    private String companySalesId;
    private String companyCustomerId;
    private String groupChatSalesId;
    private String groupChatCustomerId;
    private String accountId;
    private RealmList<String> accountSalesAgentUids;
    private RealmList<String> customerRequestIds;

    public AccountRealm() {
    }

    public AccountRealm(String accountIdFire, String companyIdA, String companyIdB, String groupChatId,
                        String groupChatCustomerId, String accountId, RealmList<String> accountUids, RealmList<String> customerRequestIds) {
        this.accountIdFire = accountIdFire;
        this.companySalesId = companyIdA;
        this.companyCustomerId = companyIdB;
        this.groupChatSalesId = groupChatId;
        this.groupChatCustomerId = groupChatCustomerId;
        this.accountId = accountId;
        this.accountSalesAgentUids = accountUids;
        this.customerRequestIds = customerRequestIds;
    }

    public AccountRealm(Account account){
        this.accountIdFire = account.getAccountIdFire();
        this.companySalesId = account.getCompanySalesId();
        this.companyCustomerId = account.getCompanyCustomerId();
        this.groupChatSalesId = account.getGroupChatSalesId();
        this.accountId = account.getAccountId();
        this.groupChatCustomerId = account.getGroupChatCustomerId();
        this.accountSalesAgentUids = createRealmList(account.getAccountUsers());
        this.customerRequestIds = createRealmListRequestIds(account.getCustomerRequestIds());
    }

    private RealmList<String> createRealmList(Map<String, String> accountUsers) {
        if(accountUsers != null) {
            RealmList<String> userList = new RealmList<>();
            for (String uid : accountUsers.values()) {
                userList.add(uid);
            }
            return userList;
        }
        return new RealmList<>();
    }

    private RealmList<String> createRealmListRequestIds(Map<String, String> requestIds) {
        if(requestIds != null) {
            RealmList<String> requests = new RealmList<>();
            for (String uid : requestIds.values()) {
                requests.add(uid);
            }
            return requests;
        }
        return new RealmList<>();
    }

    public RealmList<String> getCustomerRequestIds() {
        return customerRequestIds;
    }

    public void setCustomerRequestIds(RealmList<String> customerRequestIds) {
        this.customerRequestIds = customerRequestIds;
    }

    public RealmList<String> getAccountSalesAgentUids() {
        return accountSalesAgentUids;
    }

    public void setAccountSalesAgentUids(RealmList<String> accountSalesAgentUids) {
        this.accountSalesAgentUids = accountSalesAgentUids;
    }

    public String getGroupChatCustomerId() {
        return groupChatCustomerId;
    }

    public void setGroupChatCustomerId(String groupChatCustomerId) {
        this.groupChatCustomerId = groupChatCustomerId;
    }

    public String getAccountIdFire() {
        return accountIdFire;
    }

    public void setAccountIdFire(String accountId) {
        this.accountIdFire = accountId;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
