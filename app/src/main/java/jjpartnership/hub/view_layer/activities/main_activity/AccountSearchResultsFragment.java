package jjpartnership.hub.view_layer.activities.main_activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.utils.BaseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountSearchResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AccountSearchResultsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AccountSearchResultsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.account_results_recycler_view)RecyclerView resultsRecycler;
    @BindView(R.id.search_results_empty_frame_layout)FrameLayout noResultsLayout;
    @BindView(R.id.request_new_account_tv)TextView requestNewAccountButton;
    @BindView(R.id.no_results_user_input_tv)TextView noResultsUserInputText;

    private BaseCallback<AccountRowItem> accountSelectedCallback;
    private AccountRecyclerAdapter accountsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_search_results, container, false);
        ButterKnife.bind(this, v);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        accountSelectedCallback = new BaseCallback<AccountRowItem>() {
            @Override
            public void onResponse(AccountRowItem object) {
                mListener.onAccountSelected(object);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        return v;
    }

    public void onResultsReceived(List<AccountRowItem> items, String userInput){
        if(items != null) {
            noResultsLayout.setVisibility(View.GONE);
            if (accountsAdapter == null) {
                accountsAdapter = new AccountRecyclerAdapter(getActivity().getApplicationContext(), items, accountSelectedCallback);
                resultsRecycler.setAdapter(accountsAdapter);
            } else {
                accountsAdapter.OnDataSetChanged(items);
            }
            accountsAdapter.notifyDataSetChanged();
            if(items.size() == 0 && userInput != null && !userInput.isEmpty()){
                noResultsLayout.setVisibility(View.VISIBLE);
                noResultsUserInputText.setText(userInput);
            }else{
                resultsRecycler.setVisibility(View.VISIBLE);
            }
        }else{
            resultsRecycler.setVisibility(View.GONE);
            if(userInput != null && !userInput.isEmpty()) {
                noResultsLayout.setVisibility(View.VISIBLE);
                noResultsUserInputText.setText(userInput);
            }else{
                noResultsLayout.setVisibility(View.GONE);
            }
        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAccountSelected(AccountRowItem rowItem);
    }
}
