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
    private List<Message> messages;

    public DirectChat() {
    }

    public DirectChat(String chatId, String userIdA, String userIdB, List<Message> messages) {
        this.chatId = chatId;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        this.messages = messages;
    }

    public DirectChat(DirectChatRealm realm){
        this.chatId = realm.getChatId();
        this.userIdA = realm.getUserIdA();
        this.userIdB = realm.getUserIdB();
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
