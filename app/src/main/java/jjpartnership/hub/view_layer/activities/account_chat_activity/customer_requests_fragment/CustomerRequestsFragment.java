package jjpartnership.hub.view_layer.activities.account_chat_activity.customer_requests_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment.AssignedAgentsRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CustomerRequestsFragment extends Fragment implements CustomerRequestView{
    @BindView(R.id.request_info_linear_layout)LinearLayout requestInfo;
    @BindView(R.id.open_requests_empty_state_layout)LinearLayout openRequestsEmptyStateView;
    @BindView(R.id.open_requests_recycler_view)RecyclerView openRequestsRecycler;
    @BindView(R.id.closed_requests_empty_state_layout)LinearLayout closedRequestEmptyStateView;
    @BindView(R.id.closed_requests_recycler_view)RecyclerView closedRequestsRecycler;


    private OnFragmentInteractionListener mListener;
    private CustomerRequestPresenter presenter;
    private OpenRequestsRecyclerAdapter openRequestsAdapter;
    private BaseCallback<CustomerRequestRealm> openRequestSelectedCallback;

    public CustomerRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_requests, container, false);
        openRequestsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        initCallbacks();
        presenter = new CustomerRequestPresenterImp(this, getArguments().getString("account_id"));
        return v;
    }

    private void initCallbacks() {
        openRequestSelectedCallback = new BaseCallback<CustomerRequestRealm>() {
            @Override
            public void onResponse(CustomerRequestRealm object) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onOpenRequestsReceived(RealmList<CustomerRequestRealm> openRequests) {
        openRequestsAdapter = new OpenRequestsRecyclerAdapter(getActivity().getApplicationContext(), openRequests, openRequestSelectedCallback);
        openRequestsRecycler.setAdapter(openRequestsAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
