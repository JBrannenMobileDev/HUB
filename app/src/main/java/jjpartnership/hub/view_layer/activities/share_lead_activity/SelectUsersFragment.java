package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.TwoResponseCallback;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.user_profile_activity.UserProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelectUsersFragment extends Fragment {
    public SelectUsersFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.select_user_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.select_all_checkbox)CheckBox seeAllCheckBox;
    @BindView(R.id.no_data_layout)FrameLayout noDataLayout;

    private OnFragmentInteractionListener mListener;
    private ShareLeadUserRecyclerAdapter recyclerAdapter;
    private BaseCallback<UserRealm> userSelecctedCallback;
    private TwoResponseCallback<UserRealm, Boolean> userCheckedCallback;
    private List<UserRealm> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_users, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        filteredList = new ArrayList<>();
        initCallbacks();
        return v;
    }

    @OnClick(R.id.select_all_checkbox)
    public void onSelectAllClicked(){
        if(seeAllCheckBox.isChecked()) {
            recyclerAdapter.checkAllAgents();
        }else{
            recyclerAdapter.uncheckAllAgents();
        }
    }

    private void initCallbacks() {
        userSelecctedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm user) {
                Intent userProfileIntent = new Intent(getActivity().getApplicationContext(), UserProfileActivity.class);
                userProfileIntent.putExtra("userId", user.getUid());
                getActivity().startActivity(userProfileIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        userCheckedCallback = new TwoResponseCallback<UserRealm, Boolean>() {
            @Override
            public void onResponse(UserRealm user, Boolean checked) {
                mListener.onUserChecboxClicked(user, checked);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private void fetchData(String accountId) {
        noDataLayout.setVisibility(View.GONE);
        RealmResults<UserRealm> userList = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).sort("firstName").findAll();
        filteredList = new ArrayList<>();
        for(UserRealm user : userList){
            if(!user.getUid().equals(UserPreferences.getInstance().getUid())){
                if(user.getAccountIds().contains(accountId)) {
                    filteredList.add(user);
                }
            }
        }
        if(filteredList != null) initRecycler(filteredList);
    }

    private void initRecycler(List<UserRealm> userList) {
        recyclerAdapter = new ShareLeadUserRecyclerAdapter(getActivity(), userList, userSelecctedCallback, userCheckedCallback);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectAccountFragment.OnFragmentInteractionListener) {
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

    public void updateAccountId(String accountIdFire) {
        fetchData(accountIdFire);
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
        void onUserChecboxClicked(UserRealm user, boolean checked);
    }
}
