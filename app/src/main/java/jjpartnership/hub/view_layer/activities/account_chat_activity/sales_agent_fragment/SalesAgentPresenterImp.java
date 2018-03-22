package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.data_layer.data_models.UserColor;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class SalesAgentPresenterImp implements SalesAgentPresenter {
    private SalesAgentView fragment;
    private String userInput;
    private Realm realm;
    private String chatId;
    private String accountName;
    private String accountId;
    private UserRealm user;
    private GroupChatRealm groupChat;

    public SalesAgentPresenterImp(SalesAgentView fragment, String account_name, String account_id) {
        this.fragment = fragment;
        realm = RealmUISingleton.getInstance().getRealmInstance();
        accountName = account_name;
        accountId = account_id;
        initDataListeners();
    }

    private void initDataListeners() {
        AccountRealm account = realm.where(AccountRealm.class).equalTo("accountId", accountId).findFirst();
        chatId = account.getGroupChatSalesId();
        groupChat = realm.where(GroupChatRealm.class).equalTo("chatId", chatId).findFirst();
        user = realm.where(UserRealm.class).equalTo("uid", UserPreferences.getInstance().getUid()).findFirst();
        RealmResults<MessageRealm> messages = realm.where(MessageRealm.class).equalTo("chatId", chatId).findAll().sort("createdDate");
        messages.addChangeListener(new RealmChangeListener<RealmResults<MessageRealm>>() {
            @Override
            public void onChange(RealmResults<MessageRealm> messagesRealm) {
                fragment.onReceiveMessages(messagesRealm, getUsersColors(messagesRealm), user.getUid().equals(messagesRealm.get(messagesRealm.size() - 1).getUid()));
            }
        });
        fragment.onReceiveMessages(messages, getUsersColors(messages), user.getUid().equals(messages.get(messages.size()-1).getUid()));
    }

    private HashMap<String, Long> getUsersColors(RealmResults<MessageRealm> messagesRealm) {
        HashMap<String, Long> userColorsMap = new HashMap<>();
        for(MessageRealm message : messagesRealm){
            UserColor userColor = realm.where(UserColor.class).equalTo("uid", message.getUid()).findFirst();
            if(userColor != null) userColorsMap.put(message.getUid(), userColor.getColorId());
        }
        return userColorsMap;
    }

    @Override
    public void onUserInputChanged(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public void onSendMessageClicked() {
        if(userInput != null && !userInput.isEmpty()){
            Message newMessage = new Message();
            List<String> readByUids = new ArrayList<>();
            readByUids.add(UserPreferences.getInstance().getUid());
            newMessage.setMessageContent(userInput);
            newMessage.setCreatedByUid(UserPreferences.getInstance().getUid());
            newMessage.setChatId(chatId);
            newMessage.setCreatedDate(new Date().getTime());
            newMessage.setReadByUids(readByUids);
            newMessage.setSavedToFirebase(false);
            newMessage.setMessageOwnerName(user.getFirstName() + " " + user.getLastName());
            newMessage.setMessageThreadId(groupChat.getMessageThreadId());
            DataManager.getInstance().createNewMessage(newMessage);
        }
        fragment.resetInputText();
    }

    @Override
    public void onDestroy() {

    }
}
