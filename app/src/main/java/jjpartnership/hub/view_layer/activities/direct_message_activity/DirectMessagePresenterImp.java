package jjpartnership.hub.view_layer.activities.direct_message_activity;

import io.realm.Realm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;

/**
 * Created by Jonathan on 3/28/2018.
 */

public class DirectMessagePresenterImp implements DirectMessagePresenter{
    private DirectMessageView activity;
    private String uid;
    private Realm realm;
    private UserRealm user;
    private boolean showUserInfo;

    public DirectMessagePresenterImp(DirectMessageView activity, String uid, boolean showUserInfo) {
        this.activity = activity;
        this.uid = uid;
        this.realm = RealmUISingleton.getInstance().getRealmInstance();
        this.showUserInfo = showUserInfo;
        if(!this.showUserInfo){
            activity.hideUserInfoLayout();
        }
        fetchData();
    }

    private void fetchData() {
        user = realm.where(UserRealm.class).equalTo("uid", uid).findFirst();
        if(user != null){
            activity.setActivityTitle(user.getFirstName() + " " + user.getLastName());
        }
    }
}
