package jjpartnership.hub.data_layer.data_models;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 5/20/2018.
 */

public class AccountRowItem extends RealmObject implements Comparable<AccountRowItem>{
    @PrimaryKey
    private String accountIdFire;
    private String accountName;

    public AccountRowItem() {
    }

    @Override
    public int compareTo(@NonNull AccountRowItem item) {
        return accountName.compareToIgnoreCase(item.getAccountName());
    }

    public String getAccountIdFire() {
        return accountIdFire;
    }

    public void setAccountIdFire(String accountIdFire) {
        this.accountIdFire = accountIdFire;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
