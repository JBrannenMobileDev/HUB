package jjpartnership.hub.data_layer.data_models;

import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class MessageThreadRealm extends RealmObject{
    @PrimaryKey
    private String messageThreadId;
    private String chatId;
    private RealmList<String> currentlyTypingUserNames;

    public MessageThreadRealm() {
    }

    public MessageThreadRealm(String messageThreadId, String chatId, List<String> currentlyTypingUserNames) {
        this.messageThreadId = messageThreadId;
        this.chatId = chatId;
        this.currentlyTypingUserNames = createMessageListRealm(currentlyTypingUserNames);
    }

    public MessageThreadRealm(MessageThread thread){
        this.messageThreadId = thread.getMessageThreadId();
        this.chatId = thread.getChatId();
        this.currentlyTypingUserNames = createMessageListRealmFromMap(thread.getCurrentlyTypingUserNames());
    }

    private RealmList<String> createMessageListRealm(List<String> userNames) {
        RealmList<String> userNamesRealm = new RealmList<>();
        if(userNames != null) {
            for (String name : userNames) {
                userNamesRealm.add(name);
            }
        }
        return userNamesRealm;
    }

    private RealmList<String> createMessageListRealmFromMap(Map<String, String> userNames) {
        RealmList<String> userNamesRealm = new RealmList<>();
        if(userNames != null) {
            for (String name : userNames.values()) {
                userNamesRealm.add(name);
            }
        }
        return userNamesRealm;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public RealmList<String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(RealmList<String> currentlyTypingUserNames) {
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }
}
