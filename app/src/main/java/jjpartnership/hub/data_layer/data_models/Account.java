package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 3/6/18.
 */

public class Account {
    private String accountIdFire;
    private String accountId;
    private String companyIdA;
    private String companyIdB;
    private String groupChatId;

    public Account() {
    }

    public Account(String accountIdFire, String companyIdA, String companyIdB, String groupChatId, String accountId) {
        this.accountIdFire = accountIdFire;
        this.companyIdA = companyIdA;
        this.companyIdB = companyIdB;
        this.groupChatId = groupChatId;
        this.accountId = accountId;
    }

    public Account(AccountRealm accountRealm){
        this.accountIdFire = accountRealm.getAccountIdFire();
        this.companyIdA = accountRealm.getCompanyIdA();
        this.companyIdB = accountRealm.getCompanyIdB();
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

    public String getCompanyIdA() {
        return companyIdA;
    }

    public void setCompanyIdA(String companyIdA) {
        this.companyIdA = companyIdA;
    }

    public String getCompanyIdB() {
        return companyIdB;
    }

    public void setCompanyIdB(String companyIdB) {
        this.companyIdB = companyIdB;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }
}
