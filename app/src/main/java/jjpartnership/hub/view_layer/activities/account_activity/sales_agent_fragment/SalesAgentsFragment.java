package jjpartnership.hub.view_layer.activities.account_activity.sales_agent_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSalesChatFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SalesAgentsFragment extends Fragment implements SalesAgentView{
    @BindView(R.id.group_chats_recycler_view)RecyclerView chatsRecycler;

    private OnSalesChatFragmentInteractionListener mListener;
    private SalesAgentPresenter presenter;
    private RecyclerView.LayoutManager layoutManager;
    private SalesAgentRecyclerAdapter adapter;
    private BaseCallback<GroupChatRealm> chatSelectedCallback;

    public SalesAgentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sales_agent_chats, container, false);
        ButterKnife.bind(this, v);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        chatsRecycler.setLayoutManager(layoutManager);
        initCallback();
        presenter = new SalesAgentPresenterImp(this, getArguments().getString("account_name"),
                getArguments().getString("account_id"));
        return v;
    }

    @Override
    public void onDestroy(){
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop(){
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSalesChatFragmentInteractionListener) {
            mListener = (OnSalesChatFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSalesChatFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initCallback() {
        chatSelectedCallback = new BaseCallback<GroupChatRealm>() {
            @Override
            public void onResponse(GroupChatRealm selectedChat) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void onChatsReceived(RealmList<GroupChatRealm> groupChats, HashMap<String, Long> usersColors) {
        adapter = new SalesAgentRecyclerAdapter(getActivity().getApplicationContext(), groupChats, chatSelectedCallback, usersColors);
        chatsRecycler.setLayoutManager(layoutManager);
        chatsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public interface OnSalesChatFragmentInteractionListener {

    }
}
