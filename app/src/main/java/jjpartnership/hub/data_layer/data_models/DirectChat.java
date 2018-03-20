package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by jbrannen on 3/6/18.
 */

public class DirectChat {
    private String chatId;
    private String userIdA;
    private String userIdB;
    private Message mostRecentMessage;
    private long messageCreatedTime;
    private String messageThreadId;
    private List<String> currentlyTypingUserNames;

    public DirectChat() {
    }

    public DirectChat(String chatId, String userIdA, String userIdB, List<String> currentlyTypingUserNames,
                      Message mostRecentMessage, long messageCreatedTime, String messageThreadId) {
        this.chatId = chatId;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        this.currentlyTypingUserNames = currentlyTypingUserNames;
        this.mostRecentMessage = mostRecentMessage;
        this.messageCreatedTime = messageCreatedTime;
        this.messageThreadId = messageThreadId;
    }

    public DirectChat(DirectChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIdA = realm.getUserIdA();
        this.userIdB = realm.getUserIdB();
        this.currentlyTypingUserNames = realm.getCurrentlyTypingUserNames();
        this.mostRecentMessage = new Message(realm.getMostRecentMessage());
        this.messageCreatedTime = realm.getMessageCreatedTime();
        this.messageThreadId = realm.getMessageThreadId();
    }

    public Message getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(Message mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }

    public long getMessageCreatedTime() {
        return messageCreatedTime;
    }

    public void setMessageCreatedTime(long messageCreatedTime) {
        this.messageCreatedTime = messageCreatedTime;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public List<String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(List<String> currentlyTypingUserNames) {
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserIdA() {
        return userIdA;
    }

    public void setUserIdA(String userIdA) {
        this.userIdA = userIdA;
    }

    public String getUserIdB() {
        return userIdB;
    }

    public void setUserIdB(String userIdB) {
        this.userIdB = userIdB;
    }

}
