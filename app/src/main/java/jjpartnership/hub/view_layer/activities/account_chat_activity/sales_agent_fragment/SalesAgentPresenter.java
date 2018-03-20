package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface SalesAgentPresenter {
    void onUserInputChanged(String input);
    void onSendMessageClicked();
    void onDestroy();
}
