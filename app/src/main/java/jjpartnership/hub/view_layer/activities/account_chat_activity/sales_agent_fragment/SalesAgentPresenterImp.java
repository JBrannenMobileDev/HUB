package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import io.realm.Realm;
import jjpartnership.hub.data_layer.data_models.GroupChat;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.utils.UserPreferences;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class SalesAgentPresenterImp implements SalesAgentPresenter {
    private SalesAgentView activity;
    private String userInput;
    private Realm realm;
    private GroupChat groupChat;

    public SalesAgentPresenterImp(SalesAgentView activity) {
        this.activity = activity;
        fetchChatData();
    }

    private void fetchChatData() {
        realm = Realm.getDefaultInstance();

    }

    @Override
    public void onUserInputChanged(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public void onSendMessageClicked() {
        if(userInput != null && !userInput.isEmpty()){
            Message newMessage = new Message();
            newMessage.setMessageContent(userInput);
            newMessage.setCreatedByUid(UserPreferences.getInstance().getUid());
            newMessage.
        }
    }
}
