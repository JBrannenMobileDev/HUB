package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class MainAccountsModel extends RealmObject{
    private RealmList<RowItem> rowItems;

    public MainAccountsModel() {
    }

    public RealmList<RowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(RealmList<RowItem> rowItems) {
        this.rowItems = rowItems;
    }
}
