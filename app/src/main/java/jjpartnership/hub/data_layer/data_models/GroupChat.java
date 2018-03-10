package jjpartnership.hub.data_layer.data_models;

import java.util.List;

/**
 * Created by jbrannen on 3/6/18.
 */

public class GroupChat {
    private String chatId;
    private List<String> userIds;
    private Message mostRecentMessage;
    private long messageCreatedTime;

    public GroupChat() {
    }

    public GroupChat(String chatId, List<String> userIds, Message messages, long messageCreatedTime) {
        this.chatId = chatId;
        this.userIds = userIds;
        this.mostRecentMessage = messages;
        this.messageCreatedTime = messageCreatedTime;
    }

    public GroupChat(GroupChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIds = realm.getUserIds();
        this.mostRecentMessage = new Message(realm.getMostRecentMessage());
        this.messageCreatedTime = realm.getMessageCreatedTime();
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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Message getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(Message mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }
}
