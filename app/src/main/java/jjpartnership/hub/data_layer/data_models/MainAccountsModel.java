package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class MainAccountsModel extends RealmObject{
    public static final String PERM_ID = "main_account_model";
    @PrimaryKey
    private String permanentId;
    private RealmList<AccountRowItem> rowItems;

    public MainAccountsModel() {
        permanentId = PERM_ID;
    }

    public MainAccountsModel(RealmList<AccountRowItem> rowItems) {
        this.rowItems = rowItems;
        permanentId = PERM_ID;
    }

    public RealmList<AccountRowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(RealmList<AccountRowItem> rowItems) {
        this.rowItems = rowItems;
    }
}
