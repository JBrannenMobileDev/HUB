package jjpartnership.hub.data_layer.data_models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class MessageThreadRealm extends RealmObject{
    @PrimaryKey
    private String messageThreadId;
    private String chatId;
    private RealmList<MessageRealm> messages;

    public MessageThreadRealm() {
    }

    public MessageThreadRealm(String messageThreadId, String chatId, List<Message> messages) {
        this.messageThreadId = messageThreadId;
        this.chatId = chatId;
        this.messages = createMessageListRealm(messages);
    }

    private RealmList<MessageRealm> createMessageListRealm(List<Message> messages) {
        RealmList<MessageRealm> messagesRealm = new RealmList<>();
        for(Message message : messages){
            messagesRealm.add(new MessageRealm(message));
        }
        return messagesRealm;
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

    public RealmList<MessageRealm> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<MessageRealm> messages) {
        this.messages = messages;
    }
}
