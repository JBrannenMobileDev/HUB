package jjpartnership.hub.data_layer.data_models;

import android.support.annotation.NonNull;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class RowItem extends RealmObject implements Comparable{
    public static final String TYPE_GROUP_CHAT = "type_group_chat";
    public static final String TYPE_DIRECT = "type_direct";
    public static final String TYPE_REQUEST = "type_request";
    public static final String TYPE_DIRECT_REQUEST = "type_direct_request";

    @PrimaryKey
    private String accountId;
    private String accountName;
    private String chatId;
    private String messageOwnerName;
    private String messageContent;
    private boolean isNewMessage;
    private String itemType;
    private long messageCreatedAtTime;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public RowItem() {
        this.itemType = TYPE_GROUP_CHAT;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public boolean isNewMessage() {
        return isNewMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.isNewMessage = newMessage;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public long getMessageCreatedAtTime() {
        return messageCreatedAtTime;
    }

    public void setMessageCreatedAtTime(long messageCreatedAtTime) {
        this.messageCreatedAtTime = messageCreatedAtTime;
    }

    @Override
    public int compareTo(@NonNull Object item) {
        return this.accountName.compareToIgnoreCase(((RowItem)item).getAccountName());
    }

    public static Comparator<RowItem> createdAtComparator = new Comparator<RowItem>() {

        public int compare(RowItem item1, RowItem item2) {
            return (int) (item1.getMessageCreatedAtTime()-item2.getMessageCreatedAtTime());
        }

    };
}
