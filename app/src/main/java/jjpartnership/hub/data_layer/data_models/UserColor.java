package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/21/2018.
 */

public class UserColor extends RealmObject {
    @PrimaryKey
    private String uid;
    private long colorId;

    public UserColor() {
    }

    public UserColor(String uid, long colorId) {
        this.uid = uid;
        this.colorId = colorId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }
}
