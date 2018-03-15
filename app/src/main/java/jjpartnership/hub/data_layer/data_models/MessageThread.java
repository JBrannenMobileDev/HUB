package jjpartnership.hub.data_layer.data_models;

import java.util.List;

/**
 * Created by Jonathan on 3/15/2018.
 */

public class MessageThread {
    private String messageThreadId;
    private String chatId;
    private List<Message> messages;

    public MessageThread() {
    }

    public MessageThread(String messageThreadId, String chatId, List<Message> messages) {
        this.messageThreadId = messageThreadId;
        this.chatId = chatId;
        this.messages = messages;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
