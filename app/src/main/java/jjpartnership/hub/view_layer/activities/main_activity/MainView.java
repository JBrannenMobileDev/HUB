package jjpartnership.hub.view_layer.activities.main_activity;

import java.util.List;

import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;

/**
 * Created by jbrannen on 2/25/18.
 */

public interface MainView {
    void onRecentModelReceived(MainRecentModel recentModel);
    void onAccountModelReceived(MainAccountsModel updatedModel);
    void onDirectMessagesModelReceived(MainDirectMessagesModel directModel);
    void onGroupMessagesReceived(List<GroupChatRealm> groupChats);
    void setWelcomeMessage(String typeCustomer);
    void setPageTitle(String title);
    void setToolbarElevation(float height);
    void VibratePhone();
    void onShowAll(MainRecentModel recentModel);
}
