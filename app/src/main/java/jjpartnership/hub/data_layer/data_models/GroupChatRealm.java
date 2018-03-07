package jjpartnership.hub.data_layer.data_models;

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
    private RealmList<MessageRealm> messages;

    public GroupChatRealm() {
    }

    public GroupChatRealm(String chatId, List<String> userIds, List<Message> messages) {
        this.chatId = chatId;
        this.userIds = createUserIdList(userIds);
        this.messages = createMessageList(messages);
    }

    public GroupChatRealm(GroupChat chat){
        this.chatId = chat.getChatId();
        this.userIds = createUserIdList(chat.getUserIds());
        this.messages = createMessageList(chat.getMessages());
    }

    private RealmList<MessageRealm> createMessageList(List<Message> messages) {
        RealmList<MessageRealm> messagesRealm = new RealmList<>();
        for(Message message : messages){
            messagesRealm.add(new MessageRealm(message));
        }
        Collections.reverse(messagesRealm);
        return messagesRealm;
    }

    private RealmList<String> createUserIdList(List<String> userIds) {
        RealmList<String> userIdsRealm = new RealmList<>();
        for(String userId : userIds){
            userIdsRealm.add(userId);
        }
        Collections.reverse(userIdsRealm);
        return userIdsRealm;
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

    public RealmList<MessageRealm> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<MessageRealm> messages) {
        this.messages = messages;
    }
}
