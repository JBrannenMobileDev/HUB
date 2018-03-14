package jjpartnership.hub.data_layer.data_models;

import java.util.List;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Message{
    private String messageId;
    private String chatId;
    private String createdByUid;
    private long createdDate;
    private boolean savedToFirebase;
    private String messageContent;
    private String messageOwnerName;
    private List<String> readByUids;

    public Message() {
    }

    public Message(MessageRealm realmMessage) {
        this.messageId = realmMessage.getMessageId();
        this.createdByUid = realmMessage.getUid();
        this.messageContent = realmMessage.getMessageContent();
        this.createdDate = realmMessage.getCreatedDate();
        this.chatId = realmMessage.getChatId();
        this.messageOwnerName = realmMessage.getMessageOwnerName();
        this.readByUids = realmMessage.getReadByUids();
        this.savedToFirebase = realmMessage.isSavedToFirebase();
    }

    public boolean isSavedToFirebase() {
        return savedToFirebase;
    }

    public void setSavedToFirebase(boolean savedToFirebase) {
        this.savedToFirebase = savedToFirebase;
    }

    public List<String> getReadByUids() {
        return readByUids;
    }

    public void setReadByUids(List<String> readByUids) {
        this.readByUids = readByUids;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessageOwnerName() {
        return messageOwnerName;
    }

    public void setMessageOwnerName(String messageOwnerName) {
        this.messageOwnerName = messageOwnerName;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
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
