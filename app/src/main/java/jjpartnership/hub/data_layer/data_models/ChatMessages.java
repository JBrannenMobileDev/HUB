package jjpartnership.hub.data_layer.data_models;

import java.util.Map;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class ChatMessages {
    private Map<String, Message> messages;

    public ChatMessages() {
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }
}
