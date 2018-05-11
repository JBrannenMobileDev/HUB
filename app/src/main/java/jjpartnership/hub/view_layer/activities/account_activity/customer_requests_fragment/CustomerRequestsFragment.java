package jjpartnership.hub.view_layer.activities.account_activity.customer_requests_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.CustomerRequestRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.customer_request_chat_activity.CustomerRequestChatActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerRequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CustomerRequestsFragment extends Fragment implements CustomerRequestView{
    @BindView(R.id.open_requests_empty_state_layout)LinearLayout openRequestsEmptyStateView;
    @BindView(R.id.open_requests_recycler_view)RecyclerView openRequestsRecycler;
    @BindView(R.id.closed_requests_empty_state_layout)LinearLayout closedRequestEmptyStateView;
    @BindView(R.id.closed_requests_recycler_view)RecyclerView closedRequestsRecycler;
    @BindView(R.id.request_info_linear_layout)LinearLayout requestInfoLayout;


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
        ButterKnife.bind(this, v);
        openRequestsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        initCallbacks();
        presenter = new CustomerRequestPresenterImp(this, getArguments().getString("account_id"));
        if(!UserPreferences.getInstance().getRequestInfoVisibility()){
            requestInfoLayout.setVisibility(View.GONE);
        }
        return v;
    }

    private void initCallbacks() {
        openRequestSelectedCallback = new BaseCallback<CustomerRequestRealm>() {
            @Override
            public void onResponse(CustomerRequestRealm request) {
                Intent requestChatIntent = new Intent(getContext(), CustomerRequestChatActivity.class);
                requestChatIntent.putExtra("requestId", request.getRequestId());
                getActivity().startActivity(requestChatIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @OnClick(R.id.request_info_close)
    public void onCloseInfoClicked(){
        UserPreferences.getInstance().setRequestInfoVisibility(false);
        requestInfoLayout.setVisibility(View.GONE);
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
        if(openRequests != null && openRequests.size() > 0) {
            openRequestsEmptyStateView.setVisibility(View.GONE);
        }else{
            openRequestsEmptyStateView.setVisibility(View.VISIBLE);
        }
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
