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
    private RealmList<MessageRealm> messages;

    public DirectChatRealm() {
    }

    public DirectChatRealm(String chatId, String userIdA, String userIdB, List<Message> messages) {
        this.chatId = chatId;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        this.messages = createMessageList(messages);
    }

    public DirectChatRealm(DirectChat chat){
        this.chatId = chat.getChatId();
        this.userIdA = chat.getUserIdA();
        this.userIdB = chat.getUserIdB();
        this.messages = createMessageList(chat.getMessages());
    }

    private RealmList<MessageRealm> createMessageList(List<Message> messages) {
        RealmList<MessageRealm> messagesRealm = new RealmList<>();
        for(Message message : messages){
            messagesRealm.add(new MessageRealm(message));
        }
        Collections.reverse(messagesRealm);
        return messagesRealm;
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

    public RealmList<MessageRealm> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<MessageRealm> messages) {
        this.messages = messages;
    }
}
