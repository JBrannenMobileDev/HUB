package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class MainDirectMessagesModel extends RealmObject{
    public static final String PERM_ID = "main_direct_message_model";
    @PrimaryKey
    private String permanentId;
    private RealmList<DirectItem> directItems;

    public MainDirectMessagesModel() {
        permanentId = PERM_ID;
    }

    public MainDirectMessagesModel(RealmList<DirectItem> directItems) {
        this.directItems = directItems;
        permanentId = PERM_ID;
    }

    public RealmList<DirectItem> getDirectItems() {
        return directItems;
    }

    public void setDirectItems(RealmList<DirectItem> directItems) {
        this.directItems = directItems;
    }
}
