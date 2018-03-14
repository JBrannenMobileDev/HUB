package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.MessageRealm;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface SalesAgentView {
    void onReceiveMessages(RealmResults<MessageRealm> messagesRealm);
    void resetInputText();
}
