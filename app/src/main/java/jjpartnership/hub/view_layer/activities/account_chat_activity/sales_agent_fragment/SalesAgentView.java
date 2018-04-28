package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;


import java.util.HashMap;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface SalesAgentView {
    void onChatsReceived(RealmResults<GroupChatRealm> groupChats, HashMap<String, Long> usersColors);
    void onAllAgentsChatReceived(GroupChatRealm groupChat, String accountName);
}
