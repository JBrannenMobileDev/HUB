package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThread;
import jjpartnership.hub.data_layer.data_models.MessageThreadRealm;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class SalesAgentPresenterImp implements SalesAgentPresenter {
    private SalesAgentView fragment;
    private Realm realm;
    private String allAgentsChatId;
    private String accountName;
    private String accountId;
    private UserRealm createdByUser;
    private GroupChatRealm groupChat;
    private RealmResults<GroupChatRealm> groupChats;

    public SalesAgentPresenterImp(SalesAgentView fragment, String account_name, String account_id) {
        this.fragment = fragment;
        realm = RealmUISingleton.getInstance().getRealmInstance();
        accountName = account_name;
        accountId = account_id;
        initDataListeners();
    }

    private void initDataListeners() {
        AccountRealm account = realm.where(AccountRealm.class).equalTo("accountIdFire", accountId).findFirst();
        allAgentsChatId = account.getGroupChatSalesId();
        groupChat = realm.where(GroupChatRealm.class).equalTo("chatId", allAgentsChatId).findFirst();
        groupChats = realm.where(GroupChatRealm.class).equalTo("accountId", account.getAccountId()).findAll();
        groupChats.addChangeListener(new RealmChangeListener<RealmResults<GroupChatRealm>>() {
            @Override
            public void onChange(RealmResults<GroupChatRealm> newGroupChats) {
                fragment.onChatsReceived(groupChats, getUsersColors(newGroupChats));
            }
        });
        groupChat.addChangeListener(new RealmChangeListener<GroupChatRealm>() {
            @Override
            public void onChange(GroupChatRealm chatRealm) {
                fragment.onAllAgentsChatReceived(chatRealm, accountName);
            }
        });
        fragment.onAllAgentsChatReceived(groupChat, accountName);
        fragment.onChatsReceived(groupChats, getUsersColors(groupChats));
    }

    private HashMap<String, Long> getUsersColors(RealmResults<GroupChatRealm> groupChats) {
        HashMap<String, Long> userColorsMap = new HashMap<>();
        for(GroupChatRealm chat : groupChats){
            UserColor userColor = realm.where(UserColor.class).equalTo("uid", chat.getGroupCreatorUid()).findFirst();
            if(userColor != null) userColorsMap.put(chat.getGroupCreatorUid(), userColor.getColorId());
        }
        return userColorsMap;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStop() {
        if(groupChats != null) groupChat.removeAllChangeListeners();
    }
}
