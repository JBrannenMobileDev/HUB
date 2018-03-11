package jjpartnership.hub.view_layer.activities.main_activity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;

/**
 * Created by jbrannen on 2/25/18.
 */

public class MainPresenterImp implements MainPresenter {
    private MainView activity;
    private Realm realm;
    private MainAccountsModel dataModel;

    public MainPresenterImp(MainView activity){
        this.activity = activity;
        realm = Realm.getDefaultInstance();
        initDataListeners();
    }

    private void initDataListeners() {
        dataModel = realm.where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
        if(dataModel != null) {
            dataModel.addChangeListener(new RealmChangeListener<MainAccountsModel>() {
                @Override
                public void onChange(MainAccountsModel updatedModel) {
                    activity.onModelReceived(updatedModel);
                }
            });
            activity.onModelReceived(dataModel);
        }
    }

    @Override
    public void onSearchQuery(String newText) {

    }
}
