package jjpartnership.hub.data_layer.data_models;

import java.util.List;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class ChatMessages {
    private List<Message> chatMessages;

    public List<Message> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<Message> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
