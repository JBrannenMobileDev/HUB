package jjpartnership.hub.data_layer.data_models;

import android.support.annotation.NonNull;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/10/2018.
 */

public class RowItem extends RealmObject implements Comparable{
    @PrimaryKey
    private String accountId;
    private String accountName;
    private String messageOwnerName;
    private String messageContent;
    private boolean isNewMessage;
    private long messageCreatedAtTime;

    public RowItem() {
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
