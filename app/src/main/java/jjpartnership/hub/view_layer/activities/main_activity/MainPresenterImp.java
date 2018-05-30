package jjpartnership.hub.view_layer.activities.main_activity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.NewMessageNotification;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.FilterUtil;
import jjpartnership.hub.utils.NewMessageVibrateUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by jbrannen on 2/25/18.
 */

public class MainPresenterImp implements MainPresenter {
    private MainView activity;
    private Realm realm;
    private MainAccountsModel accountModel;
    private MainRecentModel recentModel;
    private MainDirectMessagesModel directModel;
    private RealmList<GroupChatRealm> groupChats;
    private NewMessageNotification newMessageNotification;
    private RealmResults<GroupChatRealm> allGroupChats;

    public MainPresenterImp(MainView activity){
        this.activity = activity;
        realm = RealmUISingleton.getInstance().getRealmInstance();
        fetchInitData();
    }

    private void fetchInitData() {
        if(realm != null) {
            accountModel = realm.where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
            recentModel = realm.where(MainRecentModel.class).equalTo("permanentId", MainRecentModel.PERM_ID).findFirst();
            directModel = realm.where(MainDirectMessagesModel.class).equalTo("permanentId", MainDirectMessagesModel.PERM_ID).findFirst();
            newMessageNotification = realm.where(NewMessageNotification.class).equalTo("newMessageId", NewMessageNotification.PERM_ID).findFirst();
            allGroupChats = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).findAll();
            UserRealm user = realm.where(UserRealm.class).equalTo("uid", UserPreferences.getInstance().getUid()).findFirst();
            if (user != null) {
                CompanyRealm company = realm.where(CompanyRealm.class).equalTo("companyId", user.getCompanyId()).findFirst();
                if (company != null) activity.setPageTitle(company.getName());
            }
            if (accountModel != null) {
                activity.onAccountModelReceived(accountModel);
            }
            if (recentModel != null) {
                activity.onRecentModelReceived(recentModel);
            }
            if (directModel != null) {
                activity.onDirectMessagesModelReceived(directModel);
            }
            if(allGroupChats != null){
                groupChats = FilterUtil.filterOutCustomerRequestAndAllAgentGroups(allGroupChats);
                activity.onGroupMessagesReceived(groupChats);
            }
            initDataListeners();
        }
    }

    private void initDataListeners() {
        if(accountModel != null) {
            accountModel.addChangeListener(new RealmChangeListener<MainAccountsModel>() {
                @Override
                public void onChange(MainAccountsModel updatedModel) {
                    activity.onAccountModelReceived(updatedModel);
                }
            });
            activity.onAccountModelReceived(accountModel);
        }else{
            accountModel = realm.where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
            if(accountModel != null) {
                accountModel.addChangeListener(new RealmChangeListener<MainAccountsModel>() {
                    @Override
                    public void onChange(MainAccountsModel updatedModel) {
                        activity.onAccountModelReceived(updatedModel);
                    }
                });
                activity.onAccountModelReceived(accountModel);
            }
        }
        
        if(recentModel != null){
            recentModel.addChangeListener(new RealmChangeListener<MainRecentModel>() {
                @Override
                public void onChange(MainRecentModel updatedRecent) {
                    activity.onRecentModelReceived(updatedRecent);
                }
            });
            activity.onRecentModelReceived(recentModel);
        }else{
            recentModel = realm.where(MainRecentModel.class).equalTo("permanentId", MainRecentModel.PERM_ID).findFirst();
            if(recentModel != null) {
                recentModel.addChangeListener(new RealmChangeListener<MainRecentModel>() {
                    @Override
                    public void onChange(MainRecentModel updatedRecent) {
                        activity.onRecentModelReceived(updatedRecent);
                    }
                });
                activity.onRecentModelReceived(recentModel);
            }
        }

        if(directModel != null){
            directModel.addChangeListener(new RealmChangeListener<MainDirectMessagesModel>() {
                @Override
                public void onChange(MainDirectMessagesModel updatedModel) {
                    activity.onDirectMessagesModelReceived(updatedModel);
                }
            });
            activity.onDirectMessagesModelReceived(directModel);
        }else{
            directModel = realm.where(MainDirectMessagesModel.class).equalTo("permanentId", MainDirectMessagesModel.PERM_ID).findFirst();
            if(directModel != null) {
                directModel.addChangeListener(new RealmChangeListener<MainDirectMessagesModel>() {
                    @Override
                    public void onChange(MainDirectMessagesModel updatedModel) {
                        activity.onDirectMessagesModelReceived(updatedModel);
                    }
                });
                activity.onDirectMessagesModelReceived(directModel);
            }
        }

        if(groupChats != null){
            if(allGroupChats != null) {
                allGroupChats.addChangeListener(new RealmChangeListener<RealmResults<GroupChatRealm>>() {
                    @Override
                    public void onChange(RealmResults<GroupChatRealm> groupChatRealms) {
                        groupChats = FilterUtil.filterOutCustomerRequestAndAllAgentGroups(groupChatRealms);
                        activity.onGroupMessagesReceived(groupChats);
                    }
                });
                activity.onGroupMessagesReceived(groupChats);
            }
        }else{
            allGroupChats = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).findAll();
            allGroupChats.addChangeListener(new RealmChangeListener<RealmResults<GroupChatRealm>>() {
                @Override
                public void onChange(RealmResults<GroupChatRealm> groupChatRealms) {
                    groupChats = FilterUtil.filterOutCustomerRequestAndAllAgentGroups(groupChatRealms);
                    activity.onGroupMessagesReceived(groupChats);
                }
            });
            activity.onGroupMessagesReceived(groupChats);
        }


        if(newMessageNotification != null){
            newMessageNotification.addChangeListener(new RealmChangeListener<RealmModel>() {
                @Override
                public void onChange(RealmModel realmModel) {
                    if(!newMessageNotification.hasBeenViewed() && newMessageNotification.getNewMessage() != null){
                        activity.VibratePhone();
                        NewMessageNotification copy = RealmUISingleton.getInstance().getRealmInstance().copyFromRealm(newMessageNotification);
                        copy.setHasBeenViewed(true);
                        DataManager.getInstance().updateOrInsertNewMessageNotification(copy);
                    }
                }
            });
        }
    }

    @Override
    public void onSearchQuery(String newText) {

    }

    @Override
    public void onDestroy() {
        if(accountModel != null) accountModel.removeAllChangeListeners();
        if(recentModel != null) recentModel.removeAllChangeListeners();
        if(directModel != null) directModel.removeAllChangeListeners();
        if(groupChats != null) groupChats.removeAllChangeListeners();
    }

    @Override
    public void fetchData() {
        initDataListeners();
    }

    @Override
    public void onShowAllClicked() {
        activity.onShowAll(recentModel);
    }

    @Override
    public void onRestoreRecentModel() {
        activity.onRecentModelReceived(recentModel);
    }
}
