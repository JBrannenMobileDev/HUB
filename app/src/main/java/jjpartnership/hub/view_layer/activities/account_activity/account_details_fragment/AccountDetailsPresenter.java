package jjpartnership.hub.view_layer.activities.account_activity.account_details_fragment;

import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by Jonathan on 3/26/2018.
 */

public interface AccountDetailsPresenter {
    void onDirectionsClicked();
    void onCheckboxClicked(UserRealm user, Boolean checked);
    void onNewGroupChatClicked();
}
