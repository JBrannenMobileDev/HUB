package jjpartnership.hub.view_layer.activities.account_chat_activity.customer_fragment;

import java.util.HashMap;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.MessageRealm;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface CustomerView {
    void onReceiveMessages(RealmResults<MessageRealm> messagesRealm, HashMap<String, Long> userColorMap, boolean equals);
    void resetInputText();
    void onCurrentlyTypingUpdated(String nameToDisplay);
}
