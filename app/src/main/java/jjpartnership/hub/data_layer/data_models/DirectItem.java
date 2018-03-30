package jjpartnership.hub.data_layer.data_models;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/29/2018.
 */

public class DirectItem extends RealmObject implements Comparable<DirectItem>{
    @PrimaryKey
    private String directChatId;
    private String messageOwnerUid;
    private String messageOwnerName;
    private String messageContent;
    private boolean isNewMessage;
    private long messageCreatedAtTime;

    public DirectItem() {
    }

    public DirectItem(String directChatId, String messageOwnerUid, String messageOwnerName, String messageContent, boolean isNewMessage, long messageCreatedAtTime) {
        this.directChatId = directChatId;
        this.messageOwnerUid = messageOwnerUid;
        this.messageOwnerName = messageOwnerName;
        this.messageContent = messageContent;
        this.isNewMessage = isNewMessage;
        this.messageCreatedAtTime = messageCreatedAtTime;
    }

    public String getDirectChatId() {
        return directChatId;
    }

    public void setDirectChatId(String directChatId) {
        this.directChatId = directChatId;
    }

    public String getMessageOwnerUid() {
        return messageOwnerUid;
    }

    public void setMessageOwnerUid(String messageOwnerUid) {
        this.messageOwnerUid = messageOwnerUid;
    }

    public String getMessageOwnerName() {
        return messageOwnerName;
    }

    public void setMessageOwnerName(String messageOwnerName) {
        this.messageOwnerName = messageOwnerName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isNewMessage() {
        return isNewMessage;
    }

    public void setNewMessage(boolean newMessage) {
        isNewMessage = newMessage;
    }

    public long getMessageCreatedAtTime() {
        return messageCreatedAtTime;
    }

    public void setMessageCreatedAtTime(long messageCreatedAtTime) {
        this.messageCreatedAtTime = messageCreatedAtTime;
    }

    @Override
    public int compareTo(@NonNull DirectItem directItem) {
        return (int) (directItem.getMessageCreatedAtTime() - messageCreatedAtTime);
    }
}
