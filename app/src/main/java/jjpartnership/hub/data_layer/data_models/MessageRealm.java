package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class MessageRealm extends RealmObject{
    @PrimaryKey
    private String messageId;
    private String uid;
    private long createdDate;
    private String messageContent;

    public MessageRealm(Message message) {
        this.messageId = message.getMessageId();
        this.uid = message.getCreatedByUid();
        this.messageContent = message.getMessageContent();
        this.createdDate = message.getCreatedDate();
    }

    public MessageRealm() {
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

    public String getUid() {
        return uid != null ? uid : "";
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessageContent() {
        return messageContent != null ? messageContent : "";
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
