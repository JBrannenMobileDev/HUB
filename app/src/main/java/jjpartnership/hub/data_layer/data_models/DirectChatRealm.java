package jjpartnership.hub.data_layer.data_models;

import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class DirectChatRealm extends RealmObject{
    @PrimaryKey
    private String chatId;
    private String userIdA;
    private String userIdB;
    private MessageRealm mostRecentMessage;
    private long messageCreatedTime;
    private String messageThreadId;
    private RealmList<String> currentlyTypingUserNames;

    public DirectChatRealm() {
    }

    public DirectChatRealm(String chatId, String userIdA, String userIdB, RealmList<String> currentlyTypingUserNames,
                           MessageRealm mostRecentMessage, long messageCreatedTime, String messageThreadId) {
        this.chatId = chatId;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        this.currentlyTypingUserNames = currentlyTypingUserNames;
        this.mostRecentMessage = mostRecentMessage;
        this.messageCreatedTime = messageCreatedTime;
        this.messageThreadId = messageThreadId;
    }

    public DirectChatRealm(DirectChat chat){
        this.chatId = chat.getChatId();
        this.userIdA = chat.getUserIdA();
        this.userIdB = chat.getUserIdB();
        this.currentlyTypingUserNames = createCurrentlyTypingList(chat.getCurrentlyTypingUserNames());
        this.mostRecentMessage = new MessageRealm(chat.getMostRecentMessage());
        this.messageCreatedTime = chat.getMessageCreatedTime();
        this.messageThreadId = chat.getMessageThreadId();
    }

    private RealmList<String> createCurrentlyTypingList(List<String> currentlyTypingUserNames) {
        if(currentlyTypingUserNames != null) {
            RealmList<String> realmCurrentlyTypingList = new RealmList<>();
            for (String userName : currentlyTypingUserNames) {
                realmCurrentlyTypingList.add(userName);
            }
            return realmCurrentlyTypingList;
        }else{
            return new RealmList<>();
        }
    }

    public MessageRealm getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(MessageRealm mostRecentMessage) {
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

    public RealmList<String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(RealmList<String> currentlyTypingUserNames) {
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
