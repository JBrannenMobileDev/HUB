package jjpartnership.hub.view_layer.activities.account_chat_activity.customer_requests_fragment;

import io.realm.RealmList;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;

/**
 * Created by Jonathan on 4/11/2018.
 */

public interface CustomerRequestView {
    void onOpenRequestsReceived(RealmList<CustomerRequestRealm> openRequests);
}
