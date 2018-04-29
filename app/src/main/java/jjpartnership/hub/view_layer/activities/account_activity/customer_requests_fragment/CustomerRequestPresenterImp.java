package jjpartnership.hub.view_layer.activities.account_activity.customer_requests_fragment;

import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.utils.RealmUISingleton;

/**
 * Created by Jonathan on 4/11/2018.
 */

public class CustomerRequestPresenterImp implements CustomerRequestPresenter {
    private CustomerRequestView fragment;
    private String accountId;
    private RealmList<CustomerRequestRealm> openRequests;
    private RealmList<CustomerRequestRealm> closedRequests;

    public CustomerRequestPresenterImp(CustomerRequestView fragment, String accountId) {
        this.fragment = fragment;
        this.accountId = accountId;
        openRequests = new RealmList<>();
        closedRequests = new RealmList<>();
        fetchData();
    }

    private void fetchData() {
        parseRequests(RealmUISingleton.getInstance().getRealmInstance().where(CustomerRequestRealm.class).equalTo("accountId", accountId).findAll());
    }

    private void parseRequests(RealmResults<CustomerRequestRealm> requests) {
        for(CustomerRequestRealm request : requests){
            if(request.isOpen()){
                openRequests.add(request);
            }else{
                closedRequests.add(request);
            }
        }
        fragment.onOpenRequestsReceived(openRequests);
    }
}
