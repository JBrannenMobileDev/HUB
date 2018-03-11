package jjpartnership.hub.view_layer.activities.main_activity;

import jjpartnership.hub.data_layer.data_models.MainAccountsModel;

/**
 * Created by jbrannen on 2/25/18.
 */

public interface MainView {
    void onModelReceived(MainAccountsModel updatedModel);
}
