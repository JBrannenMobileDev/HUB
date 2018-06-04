package jjpartnership.hub.view_layer.activities.account_activity.account_details_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.CommunicationsUtil;
import jjpartnership.hub.utils.TwoResponseCallback;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.account_activity.new_group_message_fragment.NewGroupMessageDialogFragment;
import jjpartnership.hub.view_layer.activities.direct_message_activity.DirectMessageActivity;
import jjpartnership.hub.view_layer.activities.user_profile_activity.UserProfileActivity;

public class AccountDetailsFragment extends Fragment implements AccountDetailsView{
    @BindView(R.id.account_details_address_tv)TextView addressTv;
    @BindView(R.id.account_details_industries_tv)TextView industriesTv;
    @BindView(R.id.assigned_agents_recycler_view)RecyclerView agentsRecycler;
    @BindView(R.id.select_all_checkbox)CheckBox selectAllCheckBox;
    @BindView(R.id.select_all_tv)TextView selectAllTv;
    @BindView(R.id.agents_group_chat_tv)TextView sendGroupChat;

    private AssignedAgentsRecyclerAdapter agentsAdapter;
    private OnAccountDetailsInteractionListener mListener;
    private AccountDetailsPresenter presenter;
    private BaseCallback<UserRealm> agentSelectedCallback;
    private BaseCallback<UserRealm> agentDirectMessageSelectedCallback;
    private BaseCallback<String> currentUserProfileSelectedCallback;
    private TwoResponseCallback<UserRealm, Boolean> checkboxSelectedCallback;
    private String currentUserUid;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_details, container, false);
        ButterKnife.bind(this, v);
        initCallbacks();
        currentUserUid = UserPreferences.getInstance().getUid();
        agentsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        presenter = new AccountDetailsPresenterImp(this, getArguments().getString("account_name"),
                getArguments().getString("account_id"));

        return v;
    }

    private void initCallbacks() {
        currentUserProfileSelectedCallback = new BaseCallback<String>() {
            @Override
            public void onResponse(String currentUserUid) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        agentSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm user) {
                Intent userProfileIntent = new Intent(getActivity().getApplicationContext(), UserProfileActivity.class);
                userProfileIntent.putExtra("userId", user.getUid());
                startActivity(userProfileIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        agentDirectMessageSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm user) {
                launchDirectMessageIntent(currentUserUid, user.getUid());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        checkboxSelectedCallback = new TwoResponseCallback<UserRealm, Boolean>() {
            @Override
            public void onResponse(UserRealm user, Boolean checked) {
                presenter.onCheckboxClicked(user, checked);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private void launchDirectMessageIntent(String uid, String toUid){
        Intent directMessageIntent = new Intent(getActivity().getApplicationContext(), DirectMessageActivity.class);
        directMessageIntent.putExtra("uid", uid);
        directMessageIntent.putExtra("toUid", toUid);
        startActivity(directMessageIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountDetailsInteractionListener) {
            mListener = (OnAccountDetailsInteractionListener) context;
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

    @OnClick(R.id.driving_directions_iv)
    public void onGetDirectionsClicked(){
        presenter.onDirectionsClicked();
    }

    @OnClick(R.id.agents_group_chat_tv)
    public void onAgentsGroupChatClicked(){
        presenter.onNewGroupChatClicked();
    }

    @OnClick(R.id.select_all_checkbox)
    public void onSelectAllCeckBoxClicked(){
        if(selectAllCheckBox.isChecked()){
            agentsAdapter.checkAllAgents();
        }else{
            agentsAdapter.uncheckAllAgents();
        }
    }

    @Override
    public void showNewGroupDialog(String accountId, ArrayList<String> agentIds, ArrayList<String> selectedAgentsIds) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NewGroupMessageDialogFragment editNameDialogFragment = NewGroupMessageDialogFragment.newInstance(accountId, agentIds, selectedAgentsIds);
        editNameDialogFragment.show(fm, "fragment_new_group");
    }

    @Override
    public void showNoAgentsSelectedToast() {
        Toast.makeText(getActivity(), "Please select at least one Sales Agent.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setNewGroupTextDissabled() {
        sendGroupChat.setTextColor(getActivity().getResources().getColor(R.color.grey_text));
    }

    @Override
    public void setNewGroupTextEnabled() {
        sendGroupChat.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void showRestrictedAccesToast() {
        Toast.makeText(getActivity(), "You must be assigned to this account to share a lead.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveCompanyData(String address, String industries) {
        addressTv.setText(address);
        industriesTv.setText(industries);
    }

    @Override
    public void onReceiveSalesAgentData(List<UserRealm> salesAgents) {
        agentsAdapter = new AssignedAgentsRecyclerAdapter(getActivity().getApplicationContext(), salesAgents,
                agentSelectedCallback, agentDirectMessageSelectedCallback, currentUserProfileSelectedCallback,
                checkboxSelectedCallback);
        agentsRecycler.setAdapter(agentsAdapter);
    }

    @Override
    public void launchDirectionsIntent(String address) {
        CommunicationsUtil.launchDirectionsIntent(address, getContext().getApplicationContext());
    }

    public interface OnAccountDetailsInteractionListener {
        void onSetPagerPage(int position);
    }
}
