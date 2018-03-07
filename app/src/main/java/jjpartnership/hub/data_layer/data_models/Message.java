package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Message{
    private String messageId;
    private String createdByUid;
    private long createdDate;
    private String messageContent;

    public Message(MessageRealm realmMessage) {
        this.messageId = realmMessage.getMessageId();
        this.createdByUid = realmMessage.getUid();
        this.messageContent = realmMessage.getMessageContent();
        this.createdDate = realmMessage.getCreatedDate();
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getMessageId() {
        return messageId != null ? messageId : "";
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCreatedByUid() {
        return createdByUid != null ? createdByUid : "";
    }

    public void setCreatedByUid(String createdByUid) {
        this.createdByUid = createdByUid;
    }

    public String getMessageContent() {
        return messageContent  != null ? messageContent : "";
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
