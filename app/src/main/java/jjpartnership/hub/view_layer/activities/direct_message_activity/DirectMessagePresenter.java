package jjpartnership.hub.view_layer.activities.direct_message_activity;

/**
 * Created by Jonathan on 3/28/2018.
 */

public interface DirectMessagePresenter {
    void onSendMessageClicked();
    void onUserInputChanged(CharSequence charSequence);
    void onSendEmailClicked();
    void onCallClicked();
    void onProfileClicked();
    void onDestroy();
}

