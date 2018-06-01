package jjpartnership.hub.view_layer.activities.direct_message_activity;

import java.util.HashMap;

import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.MessageRealm;

/**
 * Created by Jonathan on 3/28/2018.
 */

public interface DirectMessageView {
    void setActivityTitle(String s);
    void onCurrentlyTypingUpdated(String nameToDisplay);
    void onReceiveMessages(RealmResults<MessageRealm> messagesRealm, HashMap<String, Long> usersColors, boolean equals);
    void onSendCallIntent(String phoneNumber);
    void onSendEmailIntent(String email);
    void launchUserProfile(String toUid);
}
