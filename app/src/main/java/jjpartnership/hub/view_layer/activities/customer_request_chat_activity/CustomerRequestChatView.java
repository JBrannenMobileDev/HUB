package jjpartnership.hub.view_layer.activities.customer_request_chat_activity;

import java.util.HashMap;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.MessageRealm;

/**
 * Created by Jonathan on 3/28/2018.
 */

public interface CustomerRequestChatView {
    void setActivityTitle(String s);
    void onCurrentlyTypingUpdated(String nameToDisplay);
    void onReceiveMessages(RealmResults<MessageRealm> messagesRealm, HashMap<String, Long> usersColors, boolean equals);
    void onSendCallIntent(String phoneNumber);
    void onReceiveRequest(CustomerRequestRealm request);
}
