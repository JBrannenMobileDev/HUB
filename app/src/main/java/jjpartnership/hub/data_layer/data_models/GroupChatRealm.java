package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class GroupChatRealm extends RealmObject{
    @PrimaryKey
    private String chatId;
    private RealmList<String> userIds;
    private MessageRealm mostRecentMessage;
    private long messageCreatedTime;
    private String messageThreadId;
    private RealmList<RequestRealm> customerRequests;
    private RealmList<String> currentlyTypingUserNames;

    public GroupChatRealm() {
    }

    public GroupChatRealm(String chatId, List<String> userIds, Message messages, long messageCreatedTime,
                          List<String> currentlyTypingUserNames, String messageThreadId, List<Request> requests) {
        this.chatId = chatId;
        this.userIds = createUserIdList(userIds);
        this.mostRecentMessage = new MessageRealm(messages);
        this.messageCreatedTime = messageCreatedTime;
        this.currentlyTypingUserNames = createCurrentlyTypingList(currentlyTypingUserNames);
        this.messageThreadId = messageThreadId;
        this.customerRequests = createCustomerRequsts(requests);
    }

    public GroupChatRealm(GroupChat chat){
        this.chatId = chat.getChatId();
        this.userIds = createUserIdList(chat.getUserIdsList());
        this.mostRecentMessage = new MessageRealm(chat.getMostRecentMessage());
        this.messageCreatedTime = chat.getMessageCreatedTime();
        this.currentlyTypingUserNames = createCurrentlyTypingList(chat.getCurrentlyTypingUserNames());
        this.messageThreadId = chat.getMessageThreadId();
        this.customerRequests = createCustomerRequsts(chat.getCustomerRequests());
    }

    private RealmList<RequestRealm> createCustomerRequsts(List<Request> requests){
        if(requests != null) {
            RealmList<RequestRealm> realmRequests = new RealmList<>();
            for (Request request : requests) {
                realmRequests.add(new RequestRealm(request));
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

    public RealmList<RequestRealm> getCustomerRequests() {
        return customerRequests;
    }

    public void setCustomerRequests(RealmList<RequestRealm> customerRequests) {
        this.customerRequests = customerRequests;
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
