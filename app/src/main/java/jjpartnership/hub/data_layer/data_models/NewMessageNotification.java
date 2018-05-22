package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 5/21/2018.
 */

public class NewMessageNotification extends RealmObject {
    public static final String PERM_ID = "new_message_notification";
    @PrimaryKey
    private String newMessageId;
    private String messageId;
    private boolean hasBeenViewed;

    public NewMessageNotification() {
        newMessageId = PERM_ID;
    }

    public boolean hasBeenViewed() {
        return hasBeenViewed;
    }

    public void setHasBeenViewed(boolean hasBeenViewed) {
        this.hasBeenViewed = hasBeenViewed;
    }

    public String getNewMessageId() {
        return newMessageId;
    }

    public void setNewMessageId(String newMessageId) {
        this.newMessageId = newMessageId;
    }

    public String getNewMessage() {
        return messageId;
    }

    public void setNewMessage(String newMessage) {
        this.messageId = newMessage;
    }
}
