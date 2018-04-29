package jjpartnership.hub.view_layer.activities.account_activity.sales_agent_fragment;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;

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
    private RealmList<GroupChatRealm> groupChats;
    private RealmResults<GroupChatRealm> resultGroups;

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
        resultGroups = realm.where(GroupChatRealm.class).equalTo("accountId", account.getAccountIdFire()).findAll();
        filterOutRequestChats(resultGroups);

        resultGroups.addChangeListener(new RealmChangeListener<RealmResults<GroupChatRealm>>() {
            @Override
            public void onChange(RealmResults<GroupChatRealm> groupChatRealms) {
                filterOutRequestChats(groupChatRealms);
                fragment.onChatsReceived(groupChats, getUsersColors(groupChats));
            }
        });
        fragment.onChatsReceived(groupChats, getUsersColors(groupChats));
    }

    private void filterOutRequestChats(RealmResults<GroupChatRealm> groupChats){
        RealmList<GroupChatRealm> filteredChats = new RealmList<>();
        for(GroupChatRealm chat : groupChats){
            if(chat.getCustomerRequestIds() == null || chat.getCustomerRequestIds().size() == 0) {
                filteredChats.add(chat);
            }
        }
        this.groupChats = filteredChats;
    }

    private HashMap<String, Long> getUsersColors(RealmList<GroupChatRealm> groupChats) {
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
