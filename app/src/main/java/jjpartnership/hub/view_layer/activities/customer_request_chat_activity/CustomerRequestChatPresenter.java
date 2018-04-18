package jjpartnership.hub.view_layer.activities.customer_request_chat_activity;

/**
 * Created by Jonathan on 3/28/2018.
 */

public interface CustomerRequestChatPresenter {
    void onSendMessageClicked();
    void onUserInputChanged(CharSequence charSequence);
    void onSendEmailClicked();
    void onCallClicked();
    void onProfileClicked();
}

