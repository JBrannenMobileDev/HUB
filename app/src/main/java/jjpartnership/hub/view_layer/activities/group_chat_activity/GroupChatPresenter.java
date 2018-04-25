package jjpartnership.hub.view_layer.activities.group_chat_activity;

/**
 * Created by Jonathan on 3/28/2018.
 */

public interface GroupChatPresenter {
    void onSendMessageClicked();
    void onUserInputChanged(CharSequence charSequence);
    void onSeeAllMembersClicked();
}

