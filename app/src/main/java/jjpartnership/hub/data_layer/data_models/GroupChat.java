package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jbrannen on 3/6/18.
 */

public class GroupChat {
    private String chatId;
    private Map<String, String> userIds;
    private Message mostRecentMessage;
    private long messageCreatedTime;
    private String messageThreadId;
    private List<String> customerRequestIds;
    private List<String> currentlyTypingUserNames;

    public GroupChat() {
    }

    public GroupChat(String chatId, Map<String, String> userIds, Message messages, long messageCreatedTime,
                     List<String> currentlyTypingUserNames, String messageThreadId, List<String> customerRequests) {
        this.chatId = chatId;
        this.userIds = userIds;
        this.mostRecentMessage = messages;
        this.messageCreatedTime = messageCreatedTime;
        this.currentlyTypingUserNames = currentlyTypingUserNames;
        this.messageThreadId = messageThreadId;
        this.customerRequestIds = customerRequests;
    }

    public GroupChat(GroupChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIds = createUidMap(realm.getUserIds());
        this.mostRecentMessage = new Message(realm.getMostRecentMessage());
        this.messageCreatedTime = realm.getMessageCreatedTime();
        this.currentlyTypingUserNames = realm.getCurrentlyTypingUserNames();
        this.messageThreadId = realm.getMessageThreadId();
        this.customerRequestIds = realm.getCustomerRequests();
    }

    private Map<String, String> createUidMap(List<String> userIds) {
        Map<String, String> uidMap = new HashMap<>();
        for(String uid : userIds){
            uidMap.put(uid, uid);
        }
        return uidMap;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public List<String> getCustomerRequestIds() {
        return customerRequestIds;
    }

    public void setCustomerRequestIds(List<String> customerRequestIds) {
        this.customerRequestIds = customerRequestIds;
    }

    public List<String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(List<String> currentlyTypingUserNames) {
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }

    public long getMessageCreatedTime() {
        return messageCreatedTime;
    }

    public void setMessageCreatedTime(long messageCreatedTime) {
        this.messageCreatedTime = messageCreatedTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Map<String, String> getUserIds() {
        return userIds;
    }

    public List<String> getUserIdsList() {
        List<String> uidList = new ArrayList<>();
        if(userIds != null) {
            for (String uid : userIds.values()) {
                uidList.add(uid);
            }
        }
        return uidList;
    }

    public void setUserIds(Map<String, String> userIds) {
        this.userIds = userIds;
    }

    public Message getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(Message mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }
}
