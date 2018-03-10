package jjpartnership.hub.view_layer.activities.main_activity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by jbrannen on 2/25/18.
 */

public class MainPresenterImp implements MainPresenter {
    private MainView activity;
    private Realm realm;
    private RealmResults<AccountRealm> accounts;
    private RealmResults<DirectChatRealm> directChats;
    private RealmResults<GroupChatRealm> groupChats;
    private RealmResults<CompanyRealm> companies;
    private UserRealm user;

    public MainPresenterImp(MainView activity){
        this.activity = activity;
        realm = Realm.getDefaultInstance();
        initDataListeners();
    }

    private void initDataListeners() {
        user = realm.where(UserRealm.class).findFirst();
        accounts = realm.where(AccountRealm.class).findAll();
        directChats = realm.where(DirectChatRealm.class).findAll();
        groupChats = realm.where(GroupChatRealm.class).findAll();
        companies = realm.where(CompanyRealm.class).findAll();

        activity.onReceivedAccounts(accounts);

        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {

            }
        });

        accounts.addChangeListener(new RealmChangeListener<RealmResults<AccountRealm>>() {
            @Override
            public void onChange(RealmResults<AccountRealm> accountRealms) {
                activity.onReceivedAccounts(accountRealms);
            }
        });

        directChats.addChangeListener(new RealmChangeListener<RealmResults<DirectChatRealm>>() {
            @Override
            public void onChange(RealmResults<DirectChatRealm> directChatRealms) {

            }
        });

        groupChats.addChangeListener(new RealmChangeListener<RealmResults<GroupChatRealm>>() {
            @Override
            public void onChange(RealmResults<GroupChatRealm> groupChatRealms) {

            }
        });

        companies.addChangeListener(new RealmChangeListener<RealmResults<CompanyRealm>>() {
            @Override
            public void onChange(RealmResults<CompanyRealm> companyRealms) {

            }
        });
    }

    @Override
    public void onSearchQuery(String newText) {

    }
}
