package jjpartnership.hub.view_layer.activities.main_activity;

/**
 * Created by jbrannen on 2/25/18.
 */

public interface MainPresenter {
    void onSearchQuery(String newText);
    void onDestroy();
    void onShowAllRecentClicked();
    void onShowAllAccountsClicked();
    void onShowAllDirectMessagesClicked();
    void onShowAllSharedLeadsClicked();
    void onRestoreRecentModel();
    void onRestoreSharedLeads();
    void onRestoreAccounts();
    void onRestoreDirectMessages();
}
