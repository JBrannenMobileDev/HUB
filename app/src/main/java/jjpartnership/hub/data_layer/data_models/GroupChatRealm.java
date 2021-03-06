package jjpartnership.hub.data_layer.data_models;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class GroupChatRealm extends RealmObject{
    @PrimaryKey
    private String chatId;
    private String accountId;
    private String groupName;
    private String groupCreatorUid;
    private RealmList<String> userIds;
    private MessageRealm mostRecentMessage;
    private Boolean newChat;
    private long messageCreatedTime;
    private String messageThreadId;
    private RealmList<String> customerRequestIds;
    private RealmList<String> currentlyTypingUserNames;

    public GroupChatRealm() {
    }

    public GroupChatRealm(GroupChat chat){
        this.chatId = chat.getChatId();
        this.userIds = createUserIdList(chat.getUserIdsList());
        this.mostRecentMessage = new MessageRealm(chat.getMostRecentMessage());
        this.messageCreatedTime = chat.getMessageCreatedTime();
        this.currentlyTypingUserNames = createCurrentlyTypingList(chat.getCurrentlyTypingUserNames());
        this.messageThreadId = chat.getMessageThreadId();
        this.customerRequestIds = createCustomerRequsts(chat.getCustomerRequestIds());
        this.newChat = chat.isNewChat();
        this.groupName = chat.getGroupName();
        this.groupCreatorUid = chat.getGroupCreatorUid();
        this.accountId = chat.getAccountId();
    }

    private RealmList<String> createCustomerRequsts(Map<String, String> requests){
        if(requests != null) {
            RealmList<String> realmRequests = new RealmList<>();
            for (String request : requests.values()) {
                realmRequests.add(request);
            }
            return realmRequests;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createCurrentlyTypingList(List<String> currentlyTypingUserNames) {
        if(currentlyTypingUserNames != null) {
            RealmList<String> realmCurrentlyTypingList = new RealmList<>();
            for (String userName : currentlyTypingUserNames) {
                realmCurrentlyTypingList.add(userName);
            }
            return realmCurrentlyTypingList;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createUserIdList(List<String> userIds) {
        if(userIds != null) {
            RealmList<String> userIdsRealm = new RealmList<>();
            for (String userId : userIds) {
                userIdsRealm.add(userId);
            }
            Collections.reverse(userIdsRealm);
            return userIdsRealm;
        }else{
            return new RealmList<>();
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getGroupCreatorUid() {
        return groupCreatorUid;
    }

    public void setGroupCreatorUid(String groupCreatorUid) {
        this.groupCreatorUid = groupCreatorUid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean isNewChat() {
        return newChat;
    }

    public void setNewChat(Boolean newChat) {
        this.newChat = newChat;
    }

    public RealmList<String> getCustomerRequestIds() {
        return customerRequestIds;
    }

    public void setCustomerRequestIds(RealmList<String> customerRequestIds) {
        this.customerRequestIds = customerRequestIds;
    }

    public RealmList<String> getCustomerRequests() {
        return customerRequestIds;
    }

    public void setCustomerRequests(RealmList<String> customerRequests) {
        this.customerRequestIds = customerRequests;
    }

    public String getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(String messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public RealmList<String> getCurrentlyTypingUserNames() {
        return currentlyTypingUserNames;
    }

    public void setCurrentlyTypingUserNames(RealmList<String> currentlyTypingUserNames) {
        this.currentlyTypingUserNames = currentlyTypingUserNames;
    }

    public long getMessageCreatedTime() {
        return messageCreatedTime;
    }

    public void setMessageCreatedTime(long messageCreatedTime) {
        this.messageCreatedTime = messageCreatedTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public RealmList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(RealmList<String> userIds) {
        this.userIds = userIds;
    }

    public MessageRealm getMostRecentMessage() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(MessageRealm mostRecentMessage) {
        this.mostRecentMessage = mostRecentMessage;
    }
}
