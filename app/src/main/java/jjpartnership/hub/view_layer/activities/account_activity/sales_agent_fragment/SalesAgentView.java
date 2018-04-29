package jjpartnership.hub.view_layer.activities.account_activity.sales_agent_fragment;


import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface SalesAgentView {
    void onChatsReceived(RealmList<GroupChatRealm> groupChats, HashMap<String, Long> usersColors);
}
