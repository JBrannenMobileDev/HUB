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
    private String currentlyTypingUserName;

    public MessageThreadRealm() {
    }

    public MessageThreadRealm(MessageThread thread){
        this.messageThreadId = thread.getMessageThreadId();
        this.chatId = thread.getChatId();
        this.currentlyTypingUserName = thread.getCurrentlyTypingUserName();
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
