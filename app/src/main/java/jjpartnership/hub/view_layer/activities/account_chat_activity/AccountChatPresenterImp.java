package jjpartnership.hub.view_layer.activities.account_chat_activity;

import io.realm.Realm;

/**
 * Created by Jonathan on 3/13/2018.
 */

public class AccountChatPresenterImp implements AccountChatPresenter {
    private AccountChatView activity;
    private Realm realm;

    public AccountChatPresenterImp(AccountChatView activity) {
        this.activity = activity;
        realm = Realm.getDefaultInstance();
        initDataListeners();
    }

    private void initDataListeners() {

    }
}
