package jjpartnership.hub.view_layer.activities.customer_request_chat_activity;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.MessageThreadRealm;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/28/2018.
 */

public class CustomerRequestChatPresenterImp implements CustomerRequestChatPresenter{
    private CustomerRequestChatView activity;
    private String currentUserId;
    private String requestId;
    private Realm realm;
    private CustomerRequestRealm request;
    private UserRealm requester;
    private UserRealm currentUser;
    private GroupChatRealm groupChat;
    private String userInput;
    private Handler handler;
    private Runnable runnable;
    private MessageThreadRealm messageThread;
    private RealmResults<MessageRealm> messages;

    public CustomerRequestChatPresenterImp(CustomerRequestChatView activity, String requestId) {
        this.activity = activity;
        this.realm = RealmUISingleton.getInstance().getRealmInstance();
        this.requestId = requestId;
        currentUserId = UserPreferences.getInstance().getUid();
        runnable = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().updateFirebaseMessageThreadTyping(groupChat.getChatId(),
                        groupChat.getMessageThreadId(), requester.getFirstName() + " " + requester.getLastName(), false);
            }
        };
        fetchData();
    }

    private void fetchData() {
        request = realm.where(CustomerRequestRealm.class).equalTo("requestId", requestId).findFirst();
        requester = realm.where(UserRealm.class).equalTo("uid", request.getCustomerUid()).findFirst();
        currentUser = realm.where(UserRealm.class).equalTo("uid", currentUserId).findFirst();
        if(requester != null) {
            activity.setActivityTitle(requester.getFirstName() + " " + requester.getLastName());
        }
        if(request != null){
            activity.onReceiveRequest(request);
        }
        groupChat = realm.where(GroupChatRealm.class).equalTo("chatId", request.getGroupChatId()).findFirst();
        messageThread = realm.where(MessageThreadRealm.class).equalTo("messageThreadId", groupChat.getMessageThreadId()).findFirst();
        messages = realm.where(MessageRealm.class).equalTo("chatId", groupChat.getChatId()).findAll().sort("createdDate");
        if(messageThread != null) {
            messageThread.addChangeListener(new RealmChangeListener<MessageThreadRealm>() {
                @Override
                public void onChange(MessageThreadRealm threadRealm) {
                    activity.onCurrentlyTypingUpdated(getNameToDisplay(threadRealm.getCurrentlyTypingUserNames()));
                }
            });
        }
        messages.addChangeListener(new RealmChangeListener<RealmResults<MessageRealm>>() {
            @Override
            public void onChange(RealmResults<MessageRealm> messagesRealm) {
                if(messagesRealm.size() > 0) {
                    activity.onReceiveMessages(messagesRealm, getUsersColors(messagesRealm), currentUserId.equals(messagesRealm.get(messagesRealm.size() - 1).getUid()));
                    updateAllMessagesToRead(messagesRealm);
                }
            }
        });
        if(messages.size() > 0) {
            activity.onReceiveMessages(messages, getUsersColors(messages), currentUserId.equals(messages.get(messages.size() - 1).getUid()));
            updateAllMessagesToRead(messages);
        }
    }

    private HashMap<String, Long> getUsersColors(RealmResults<MessageRealm> messagesRealm) {
        HashMap<String, Long> userColorsMap = new HashMap<>();
        for(MessageRealm message : messagesRealm){
            UserColor userColor = realm.where(UserColor.class).equalTo("uid", message.getUid()).findFirst();
            if(userColor != null) userColorsMap.put(message.getUid(), userColor.getColorId());
        }
        return userColorsMap;
    }

    private void updateAllMessagesToRead(RealmResults<MessageRealm> messages) {
        DataManager.getInstance().updateMessages(messages);
    }

    private String getNameToDisplay(RealmList<String> currentlyTypingUserNames) {
        String nameToDisplay = null;
        if(currentlyTypingUserNames != null && currentlyTypingUserNames.size() > 0){
            for(int i = currentlyTypingUserNames.size()-1; i >= 0; i--){
                if(!currentlyTypingUserNames.get(i).equals(currentUser.getFirstName() + " " + currentUser.getLastName())){
                    return currentlyTypingUserNames.get(i);
                }
            }
        }
        return nameToDisplay;
    }

    @Override
    public void onSendMessageClicked() {
        DataManager.getInstance().updateFirebaseMessageThreadTyping(groupChat.getChatId(),
                groupChat.getMessageThreadId(), currentUser.getFirstName() + " " + currentUser.getLastName(), false);
        if(userInput != null && !userInput.isEmpty()){
            Message newMessage = new Message();
            List<String> readByUids = new ArrayList<>();
            readByUids.add(UserPreferences.getInstance().getUid());
            newMessage.setMessageContent(userInput);
            newMessage.setCreatedByUid(UserPreferences.getInstance().getUid());
            newMessage.setChatId(groupChat.getChatId());
            newMessage.setCreatedDate(new Date().getTime());
            newMessage.setReadByUids(readByUids);
            newMessage.setSavedToFirebase(false);
            newMessage.setMessageOwnerName(requester.getFirstName() + " " + requester.getLastName());
            newMessage.setMessageThreadId(groupChat.getMessageThreadId());
            DataManager.getInstance().createNewMessage(newMessage);
        }
    }

    private void updateCurrentlyTypingFirebase() {
        if(handler != null) handler.removeCallbacks(runnable);
        DataManager.getInstance().updateFirebaseMessageThreadTyping(groupChat.getChatId(),
                groupChat.getMessageThreadId(), currentUser.getFirstName() + " " + currentUser.getLastName(), true);
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onUserInputChanged(CharSequence updatedUserInput) {
        userInput = updatedUserInput.toString();
        updateCurrentlyTypingFirebase();
    }

    @Override
    public void onProfileClicked() {

    }
}
