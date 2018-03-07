package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class AccountRealm extends RealmObject{
    @PrimaryKey
    private String accountIdFire;
    private String companyIdA;
    private String companyIdB;
    private String groupChatId;
    private String accountId;

    public AccountRealm() {
    }

    public AccountRealm(String accountIdFire, String companyIdA, String companyIdB, String groupChatId, String accountId) {
        this.accountIdFire = accountIdFire;
        this.companyIdA = companyIdA;
        this.companyIdB = companyIdB;
        this.groupChatId = groupChatId;
        this.accountId = accountId;
    }

    public AccountRealm(Account account){
        this.accountIdFire = account.getAccountIdFire();
        this.companyIdA = account.getCompanyIdA();
        this.companyIdB = account.getCompanyIdB();
        this.groupChatId = account.getGroupChatId();
        this.accountId = account.getAccountId();
    }

    public String getAccountIdFire() {
        return accountIdFire;
    }

    public void setAccountIdFire(String accountId) {
        this.accountIdFire = accountId;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
