package jjpartnership.hub.data_layer.data_models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class MessageThread {
    private String messageThreadId;
    private String chatId;
    private Map<String,String> currentlyTypingUserNames;

    public MessageThread() {
    }

    public MessageThread(String messageThreadId, String chatId, Map<String,String> currentlyTypingUserNames) {
        this.messageThreadId = messageThreadId;
        this.chatId = chatId;
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }

    public MessageThread(MessageThreadRealm realm){
        this.messageThreadId = realm.getMessageThreadId();
        this.chatId = realm.getChatId();
        this.currentlyTypingUserNames = createUserNamesMap(realm.getCurrentlyTypingUserNames());
    }

    private Map<String, String> createUserNamesMap(RealmList<String> currentlyTypingUserNames) {
        Map<String, String> userNamesMap = new HashMap<>();
        for(String userName : currentlyTypingUserNames){
            userNamesMap.put(userName, userName);
        }
        return userNamesMap;
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

    public Map<String,String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(Map<String,String> currentlyTypingUserNames) {
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }
}
