package jjpartnership.hub.view_layer.activities.account_activity.account_details_fragment;


import java.util.ArrayList;
import java.util.List;

import jjpartnership.hub.data_layer.data_models.UserRealm;

/**
 * Created by Jonathan on 3/26/2018.
 */

public interface AccountDetailsView {
    void onReceiveCompanyData(String address, String industries);
    void onReceiveSalesAgentData(List<UserRealm> salesAgents);
    void launchDirectionsIntent(String address);
    void showNewGroupDialog(String accountId, ArrayList<String> agentIds, ArrayList<String> selectedAgentsIds);
    void showNoAgentsSelectedToast();
    void setNewGroupTextDissabled();
    void setNewGroupTextEnabled();
    void showRestrictedAccesToast();
}



