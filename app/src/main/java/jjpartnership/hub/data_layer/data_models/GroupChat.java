package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by jbrannen on 3/6/18.
 */

public class GroupChat {
    private String chatId;
    private List<String> userIds;
    private List<Message> messages;

    public GroupChat() {
    }

    public GroupChat(String chatId, List<String> userIds, List<Message> messages) {
        this.chatId = chatId;
        this.userIds = userIds;
        this.messages = messages;
    }

    public GroupChat(GroupChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIds = realm.getUserIds();
        this.messages = createMessageList(realm.getMessages());
    }

    private List<Message> createMessageList(RealmList<MessageRealm> messagesRealm) {
        List<Message> messageList = new ArrayList<>();
        for(MessageRealm messageRealm : messagesRealm){
            messageList.add(new Message(messageRealm));
        }
        Collections.reverse(messageList);
        return messageList;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
