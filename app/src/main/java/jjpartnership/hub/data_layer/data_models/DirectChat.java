package jjpartnership.hub.data_layer.data_models;

import java.util.List;

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
    private String requestMessage;
    private boolean isFromCustomerRequest;
    private boolean isRequestOpen;
    private List<String> currentlyTypingUserNames;

    public DirectChat() {
    }

    public DirectChat(String chatId, String userIdA, String userIdB, List<String> currentlyTypingUserNames,
                      Message mostRecentMessage, long messageCreatedTime, String messageThreadId,
                      boolean isFromCustomerRequest, boolean isRequestOpen, String requestMessage) {
        this.chatId = chatId;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        this.currentlyTypingUserNames = currentlyTypingUserNames;
        this.mostRecentMessage = mostRecentMessage;
        this.messageCreatedTime = messageCreatedTime;
        this.messageThreadId = messageThreadId;
        this.isFromCustomerRequest = isFromCustomerRequest;
        this.isRequestOpen = isRequestOpen;
        this.requestMessage = requestMessage;
    }

    public DirectChat(DirectChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIdA = realm.getUserIdA();
        this.userIdB = realm.getUserIdB();
        this.currentlyTypingUserNames = realm.getCurrentlyTypingUserNames();
        this.mostRecentMessage = new Message(realm.getMostRecentMessage());
        this.messageCreatedTime = realm.getMessageCreatedTime();
        this.messageThreadId = realm.getMessageThreadId();
        this.isFromCustomerRequest = realm.isFromCustomerRequest();
        this.isRequestOpen = realm.isRequestOpen();
        this.requestMessage = realm.getRequestMessage();
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public boolean isRequestOpen() {
        return isRequestOpen;
    }

    public void setRequestOpen(boolean requestOpen) {
        isRequestOpen = requestOpen;
    }

    public boolean isFromCustomerRequest() {
        return isFromCustomerRequest;
    }

    public void setFromCustomerRequest(boolean fromCustomerRequest) {
        isFromCustomerRequest = fromCustomerRequest;
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
