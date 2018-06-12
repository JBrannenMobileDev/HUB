package jjpartnership.hub.view_layer.activities.search_activities;

import android.content.Context;
import android.os.Bundle;
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
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsersSearchResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class UsersSearchResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public UsersSearchResultFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.users_results_recycler_view)RecyclerView resultsRecycler;
    @BindView(R.id.search_results_empty_frame_layout)FrameLayout noResultsLayout;
    @BindView(R.id.no_results_user_input_tv)TextView noResultsUserInputText;

    private BaseCallback<UserRealm> userSelectedCallback;
    private BaseCallback<UserRealm> directMessageSelectedCallback;
    private UserSearchResultsRecyclerAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users_search_result, container, false);
        ButterKnife.bind(this, v);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        initCallbacks();
        return v;
    }

    private void initCallbacks() {
        userSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm user) {
                mListener.onUserSelected(user);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        directMessageSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm user) {
                mListener.onDirectMessageSelected(user);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    public void onResultsReceived(List<UserRealm> items, String userInput){
        if(items != null) {
            noResultsLayout.setVisibility(View.GONE);
            if (usersAdapter == null) {
                usersAdapter = new UserSearchResultsRecyclerAdapter(getActivity().getApplicationContext(), items, userSelectedCallback, directMessageSelectedCallback,null);
                resultsRecycler.setAdapter(usersAdapter);
            } else {
                usersAdapter.OnDataSetChanged(items);
            }
            usersAdapter.notifyDataSetChanged();
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
        if (context instanceof AccountSearchResultsFragment.OnFragmentInteractionListener) {
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
        void onUserSelected(UserRealm user);
        void onDirectMessageSelected(UserRealm user);
    }
}
