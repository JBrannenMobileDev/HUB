package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 3/6/18.
 */

public class Account {
    private String accountIdFire;
    private String accountId;
    private String companySalesId;
    private String companyCustomerId;
    private String groupChatId;

    public Account() {
    }

    public Account(String accountIdFire, String companyIdA, String companyIdB, String groupChatId, String accountId) {
        this.accountIdFire = accountIdFire;
        this.companySalesId = companyIdA;
        this.companyCustomerId = companyIdB;
        this.groupChatId = groupChatId;
        this.accountId = accountId;
    }

    public Account(AccountRealm accountRealm){
        this.accountIdFire = accountRealm.getAccountIdFire();
        this.companySalesId = accountRealm.getCompanySalesId();
        this.companyCustomerId = accountRealm.getCompanyCustomerId();
        this.groupChatId = accountRealm.getGroupChatId();
        this.accountId = accountRealm.getAccountId();
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

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }
}
