package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Message{
    private String messageId;
    private String uid;
    private String messageContent;

    public Message(MessageRealm realmMessage) {
        this.messageId = realmMessage.getMessageId();
        this.uid = realmMessage.getUid();
        this.messageContent = realmMessage.getMessageContent();
    }

    public String getMessageId() {
        return messageId != null ? messageId : "";
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUid() {
        return uid != null ? uid : "";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessageContent() {
        return messageContent  != null ? messageContent : "";
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
