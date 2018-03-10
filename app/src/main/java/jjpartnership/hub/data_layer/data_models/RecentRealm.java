package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jonathan on 3/9/2018.
 */

public class RecentRealm extends RealmObject {
    @PrimaryKey
    private String chatId;
    public static final String TYPE_GROUP = "group_chat";
    public static final String TYPE_DIRECT_CHAT = "direct_chat";
    private String interactionOwnerName;
    private String chatType;
    private String accountId;
    private String mostRecentMessage;

    public RecentRealm() {
    }

    public RecentRealm(String chatId, String interactionOwnerName, String chatType, String accountId, String mostRecentMessage) {
        this.chatId = chatId;
        this.interactionOwnerName = interactionOwnerName;
        this.chatType = chatType;
        this.accountId = accountId;
        this.mostRecentMessage = mostRecentMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public static String getTypeGroup() {
        return TYPE_GROUP;
    }

    public static String getTypeDirectChat() {
        return TYPE_DIRECT_CHAT;
    }

    public String getInteractionOwnerName() {
        return interactionOwnerName;
    }

    public void setInteractionOwnerName(String interactionOwnerName) {
        this.interactionOwnerName = interactionOwnerName;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(String mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }
}
