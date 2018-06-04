package jjpartnership.hub.view_layer.activities.account_activity;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class AccountChatPresenterImp implements AccountChatPresenter {
    private AccountChatView activity;

    public AccountChatPresenterImp(AccountChatView activity) {
        this.activity = activity;
        initDataListeners();
    }

    private void initDataListeners() {

    }
}
