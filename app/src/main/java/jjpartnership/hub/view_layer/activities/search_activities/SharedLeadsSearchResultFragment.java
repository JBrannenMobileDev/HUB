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
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.view_layer.activities.main_activity.GroupMessagesRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SharedLeadsSearchResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SharedLeadsSearchResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SharedLeadsSearchResultFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.users_results_recycler_view)RecyclerView resultsRecycler;
    @BindView(R.id.search_results_empty_frame_layout)FrameLayout noResultsLayout;
    @BindView(R.id.no_results_user_input_tv)TextView noResultsUserInputText;

    private BaseCallback<GroupChatRealm> chatSelectedCallback;
    private GroupMessagesRecyclerAdapter groupChatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shared_leads_search_result, container, false);
        ButterKnife.bind(this, v);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        initCallbacks();
        return v;
    }

    private void initCallbacks() {
        chatSelectedCallback = new BaseCallback<GroupChatRealm>() {
            @Override
            public void onResponse(GroupChatRealm chat) {
                mListener.onChatSelected(chat);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    public void onResultsReceived(List<GroupChatRealm> items, String userInput){
        if(items != null) {
            noResultsLayout.setVisibility(View.GONE);
            if (groupChatAdapter == null) {
                groupChatAdapter = new GroupMessagesRecyclerAdapter(getActivity().getApplicationContext(), items, chatSelectedCallback);
                resultsRecycler.setAdapter(groupChatAdapter);
            } else {
                groupChatAdapter.OnDataSetChanged(items);
            }
            groupChatAdapter.notifyDataSetChanged();
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
        void onChatSelected(GroupChatRealm chat);
    }
}
