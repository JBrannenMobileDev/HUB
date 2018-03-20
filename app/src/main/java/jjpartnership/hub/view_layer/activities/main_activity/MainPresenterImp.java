package jjpartnership.hub.view_layer.activities.main_activity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by jbrannen on 2/25/18.
 */

public class MainPresenterImp implements MainPresenter {
    private MainView activity;
    private Realm realm;
    private MainAccountsModel dataModel;
    private MainRecentModel recentModel;

    public MainPresenterImp(MainView activity){
        this.activity = activity;
        realm = RealmUISingleton.getInstance().getRealmInstance();
        if(UserPreferences.getInstance().getUserType().equalsIgnoreCase(UserRealm.TYPE_SALES)){
            activity.setWelcomeMessage(UserRealm.TYPE_SALES);
        }else{
            activity.setWelcomeMessage(UserRealm.TYPE_CUSTOMER);
        }
        initDataListeners();
    }

    private void initDataListeners() {
        dataModel = realm.where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
        recentModel = realm.where(MainRecentModel.class).equalTo("permanentId", MainRecentModel.PERM_ID).findFirst();
        if(dataModel != null) {
            dataModel.addChangeListener(new RealmChangeListener<MainAccountsModel>() {
                @Override
                public void onChange(MainAccountsModel updatedModel) {
                    activity.onAccountModelReceived(updatedModel);
                }
            });
            activity.onAccountModelReceived(dataModel);
        }
        if(recentModel != null){
            recentModel.addChangeListener(new RealmChangeListener<MainRecentModel>() {
                @Override
                public void onChange(MainRecentModel updatedRecent) {
                    activity.onRecentModelReceived(updatedRecent);
                }
            });
            activity.onRecentModelReceived(recentModel);
        }

    }

    @Override
    public void onSearchQuery(String newText) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void fetchData() {
        initDataListeners();
    }
}
