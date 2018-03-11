package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class MainRecentModel extends RealmObject{
    public static final String PERM_ID = "main_recent_model";
    @PrimaryKey
    private String permanentId;
    private RealmList<RecentRowItem> rowItems;

    public MainRecentModel() {
        permanentId = PERM_ID;
    }

    public MainRecentModel(RealmList<RecentRowItem> rowItems) {
        this.rowItems = rowItems;
        permanentId = PERM_ID;
    }

    public RealmList<RecentRowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(RealmList<RecentRowItem> rowItems) {
        this.rowItems = rowItems;
    }
}
