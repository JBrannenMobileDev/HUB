package jjpartnership.hub.view_layer.activities.direct_message_activity;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
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

public class DirectMessagePresenterImp implements DirectMessagePresenter{
    private DirectMessageView activity;
    private String toUid;
    private String uid;
    private Realm realm;
    private UserRealm user;
    private DirectChatRealm directChat;
    private String userInput;
    private Handler handler;
    private Runnable runnable;
    private MessageThreadRealm messageThread;
    private RealmResults<MessageRealm> messages;
    private UserRealm userCurrent;

    public DirectMessagePresenterImp(DirectMessageView activity, String uid, String toUid) {
        this.activity = activity;
        this.toUid = toUid;
        this.realm = RealmUISingleton.getInstance().getRealmInstance();
        userCurrent = realm.where(UserRealm.class).equalTo("uid", uid).findFirst();

        this.uid = uid;
        runnable = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().updateFirebaseMessageThreadTyping(directChat.getChatId(),
                        directChat.getMessageThreadId(), user.getFirstName() + " " + user.getLastName(), false);
            }
        };
        fetchData();
    }

    @Override
    public void onDestroy(){
        if(messages != null) messages.removeAllChangeListeners();
        if(messageThread != null) messageThread.removeAllChangeListeners();
    }

    private void fetchData() {
        user = realm.where(UserRealm.class).equalTo("uid", toUid).findFirst();
        if(user != null) {
            activity.setActivityTitle(user.getFirstName() + " " + user.getLastName());
        }
        directChat = realm.where(DirectChatRealm.class).equalTo("userIdB", toUid).findFirst();
        if(directChat == null){
            directChat = realm.where(DirectChatRealm.class).equalTo("userIdA", toUid).findFirst();
        }
        if(directChat == null){
            DataManager.getInstance().createNewDirectChat(uid, toUid);
            directChat = realm.where(DirectChatRealm.class).equalTo("userIdB", toUid).findFirst();
        }

        messageThread = realm.where(MessageThreadRealm.class).equalTo("messageThreadId", directChat.getMessageThreadId()).findFirst();
        messages = realm.where(MessageRealm.class).equalTo("chatId", directChat.getChatId()).findAll().sort("createdDate");
        if(messageThread != null) {
            messageThread.addChangeListener(new RealmChangeListener<MessageThreadRealm>() {
                @Override
                public void onChange(MessageThreadRealm threadRealm) {
                    activity.onCurrentlyTypingUpdated(getNameToDisplay(threadRealm.getCurrentlyTypingUserName()));
                }
            });
        }
        messages.addChangeListener(new RealmChangeListener<RealmResults<MessageRealm>>() {
            @Override
            public void onChange(RealmResults<MessageRealm> messagesRealm) {
                if(messagesRealm.size() > 0) {
                    activity.onReceiveMessages(messagesRealm, getUsersColors(messagesRealm), uid.equals(messagesRealm.get(messagesRealm.size() - 1).getUid()));
                    updateAllMessagesToRead(messagesRealm);
                }
            }
        });
        if(messages.size() > 0) {
            activity.onReceiveMessages(messages, getUsersColors(messages), uid.equals(messages.get(messages.size() - 1).getUid()));
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
        DataManager.getInstance().updateMessages(messages, directChat);
    }

    private String getNameToDisplay(String currentlyTypingUserName) {
        if(currentlyTypingUserName != null) {
            if (!currentlyTypingUserName.equals(userCurrent.getFirstName() + " " + userCurrent.getLastName())) {
                return currentlyTypingUserName;
            }
        }
        return null;
    }

    @Override
    public void onSendMessageClicked() {
        DataManager.getInstance().updateFirebaseMessageThreadTyping(directChat.getChatId(),
                directChat.getMessageThreadId(), userCurrent.getFirstName() + " " + userCurrent.getLastName(), false);
        if(userInput != null && !userInput.isEmpty()){
            Message newMessage = new Message();
            List<String> readByUids = new ArrayList<>();
            readByUids.add(UserPreferences.getInstance().getUid());
            newMessage.setMessageContent(userInput);
            newMessage.setCreatedByUid(UserPreferences.getInstance().getUid());
            newMessage.setChatId(directChat.getChatId());
            newMessage.setCreatedDate(new Date().getTime());
            newMessage.setReadByUids(readByUids);
            newMessage.setSavedToFirebase(false);
            newMessage.setMessageOwnerName(userCurrent.getFirstName() + " " + userCurrent.getLastName());
            newMessage.setMessageThreadId(directChat.getMessageThreadId());
            DataManager.getInstance().createNewDirectMessage(newMessage);
        }
    }

    private void updateCurrentlyTypingFirebase() {
        if(handler != null) handler.removeCallbacks(runnable);
        DataManager.getInstance().updateFirebaseMessageThreadTyping(directChat.getChatId(),
                directChat.getMessageThreadId(), userCurrent.getFirstName() + " " + userCurrent.getLastName(), true);
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onUserInputChanged(CharSequence updatedUserInput) {
        userInput = updatedUserInput.toString();
        updateCurrentlyTypingFirebase();
    }

    @Override
    public void onSendEmailClicked() {
        activity.onSendEmailIntent(user.getEmail());
    }

    @Override
    public void onCallClicked() {
        activity.onSendCallIntent(user.getPhoneNumber());
    }

    @Override
    public void onProfileClicked() {

    }
}
