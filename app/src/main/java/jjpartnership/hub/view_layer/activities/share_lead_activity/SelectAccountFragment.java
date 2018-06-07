package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.main_activity.AccountRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelectAccountFragment extends Fragment {

    @BindView(R.id.select_account_recycler_view)RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private ShareLeadAccountRecyclerAdapter recyclerAdapter;

    public SelectAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_account, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        fetchData();
        return v;
    }

    private void fetchData() {
        MainAccountsModel accountsModel = RealmUISingleton.getInstance().getRealmInstance().where(MainAccountsModel.class).equalTo("permanentId", MainAccountsModel.PERM_ID).findFirst();
        if(accountsModel != null) initRecycler(accountsModel.getRowItems());
    }

    private void initRecycler(List<AccountRowItem> filteredList) {
        BaseCallback<AccountRowItem> accountSelectedCallback = new BaseCallback<AccountRowItem>() {
            @Override
            public void onResponse(AccountRowItem item) {
                mListener.onAccountSelected(item);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        recyclerAdapter = new ShareLeadAccountRecyclerAdapter(getActivity(), filteredList, accountSelectedCallback);
        recyclerView.setAdapter(recyclerAdapter);
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
        // TODO: Update argument type and name
        void onAccountSelected(AccountRowItem account);
    }
}
