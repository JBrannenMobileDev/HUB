package jjpartnership.hub.data_layer.data_models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class MessageRealm extends RealmObject{
    private String messageId;
    private String chatId;
    private String uid;
    private String messageThreadId;
    @PrimaryKey
    private long createdDate;
    private boolean savedToFirebase;
    private String messageContent;
    private String messageOwnerName;
    private RealmList<String> readByUids;
    private RealmList<String> receivedByUids;

    public MessageRealm(Message message) {
        if(message != null) {
            this.messageId = message.getMessageId();
            this.uid = message.getCreatedByUid();
            this.messageContent = message.getMessageContent();
            this.createdDate = message.getCreatedDate();
            this.chatId = message.getChatId();
            this.messageOwnerName = message.getMessageOwnerName();
            this.readByUids = createReadByUidList(message.getReadByUids());
            this.receivedByUids = createReceivedByUidList(message.getReceivedByUids());
            this.savedToFirebase = message.isSavedToFirebase();
            this.messageThreadId = message.getMessageThreadId();
        }
    }

    private RealmList<String> createReadByUidList(List<String> readByUids) {
        RealmList<String> realmReadByUids = new RealmList<>();
        for(String uid : readByUids){
            realmReadByUids.add(uid);
        }
        return realmReadByUids;
    }

    private RealmList<String> createReceivedByUidList(List<String> receivedByUids) {
        RealmList<String> realmRecievedByUids = new RealmList<>();
        if(receivedByUids != null) {
            for (String uid : receivedByUids) {
                realmRecievedByUids.add(uid);
            }
        }
        return realmRecievedByUids;
    }

    public MessageRealm() {
    }

    public RealmList<String> getReceivedByUids() {
        return receivedByUids;
    }

    public void setReceivedByUids(RealmList<String> receivedByUids) {
        this.receivedByUids = receivedByUids;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public boolean isSavedToFirebase() {
        return savedToFirebase;
    }

    public void setSavedToFirebase(boolean savedToFirebase) {
        this.savedToFirebase = savedToFirebase;
    }

    public RealmList<String> getReadByUids() {
        return readByUids;
    }

    public void setReadByUids(RealmList<String> readByUids) {
        this.readByUids = readByUids;
    }

    public String getMessageOwnerName() {
        return messageOwnerName;
    }

    public void setMessageOwnerName(String messageOwnerName) {
        this.messageOwnerName = messageOwnerName;
    }

    public String getChatId() {
        return chatId;
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
