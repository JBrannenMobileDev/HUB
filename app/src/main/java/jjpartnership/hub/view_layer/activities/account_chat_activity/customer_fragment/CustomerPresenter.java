package jjpartnership.hub.view_layer.activities.account_chat_activity.customer_fragment;

/**
 * Created by Jonathan on 3/13/2018.
 */

public interface CustomerPresenter {
    void onUserInputChanged(String input);
    void onSendMessageClicked();
    void onDestroy();
    void onStop();
}
