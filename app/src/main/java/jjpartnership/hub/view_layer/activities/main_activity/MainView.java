package jjpartnership.hub.view_layer.activities.main_activity;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.AccountRealm;

/**
 * Created by jbrannen on 2/25/18.
 */

public interface MainView {
    void onReceivedAccounts(RealmResults<AccountRealm> accountRealms);
}
