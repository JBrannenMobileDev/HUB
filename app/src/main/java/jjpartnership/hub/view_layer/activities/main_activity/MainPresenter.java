package jjpartnership.hub.view_layer.activities.main_activity;

/**
 * Created by jbrannen on 2/25/18.
 */

public interface MainPresenter {
    void onSearchQuery(String newText);
    void onDestroy();
    void fetchData();
    void onShowAllClicked();
    void onRestoreRecentModel();
    void onSearch(String query);
}
