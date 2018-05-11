package jjpartnership.hub.data_layer.data_models;

import java.util.HashMap;

import io.realm.RealmList;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class MessageThread {
    private String messageThreadId;
    private String chatId;
    private String currentlyTypingUserName;

    public MessageThread() {
    }

    public MessageThread(String messageThreadId, String chatId, String currentlyTypingUserNames) {
        this.messageThreadId = messageThreadId;
        this.chatId = chatId;
        this.currentlyTypingUserName = currentlyTypingUserNames;
    }

    public MessageThread(MessageThreadRealm realm){
        this.messageThreadId = realm.getMessageThreadId();
        this.chatId = realm.getChatId();
        this.currentlyTypingUserName = realm.getCurrentlyTypingUserName();
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

    public String getCurrentlyTypingUserName() {
        return currentlyTypingUserName;
    }

    public void setCurrentlyTypingUserName(String currentlyTypingUserName) {
        this.currentlyTypingUserName = currentlyTypingUserName;
    }
}
