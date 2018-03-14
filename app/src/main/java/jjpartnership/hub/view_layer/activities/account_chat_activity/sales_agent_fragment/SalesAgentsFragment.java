package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.Message;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSalesChatFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SalesAgentsFragment extends Fragment implements SalesAgentView{
    @BindView(R.id.chat_empty_state)TextView emptyStateMessage;
    @BindView(R.id.chat_recycler_view)RecyclerView chatRecycler;

    private OnSalesChatFragmentInteractionListener mListener;
    private SalesAgentPresenter presenter;
    private LinearLayoutManager layoutManager;
    private SalesAgentRecyclerAdapter adapter;
    private BaseCallback<MessageRealm> messageSelectedCallback;

    public SalesAgentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, v);

        presenter = new SalesAgentPresenterImp(this, getArguments().getString("account_name"),
                getArguments().getString("account_id"));

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(layoutManager);
        initAdapters();
        return v;
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

    public void onUserInputChanged(CharSequence charSequence) {
        presenter.onUserInputChanged(charSequence.toString());
    }

    public void onSendMessageClicked() {
        presenter.onSendMessageClicked();
        hideSoftKeyboard();
    }

    private void initAdapters() {
        messageSelectedCallback = new BaseCallback<MessageRealm>() {
            @Override
            public void onResponse(MessageRealm selectedMessage) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        chatRecycler.setAdapter(adapter);
    }

    @Override
    public void onReceiveMessages(RealmResults<MessageRealm> messagesRealm) {
        if(messagesRealm.size() > 0){
            emptyStateMessage.setVisibility(View.GONE);
        }else {
            emptyStateMessage.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            adapter = new SalesAgentRecyclerAdapter(getActivity().getApplicationContext(), messagesRealm, messageSelectedCallback);
            chatRecycler.setAdapter(adapter);
        } else {
            adapter.OnDataSetChanged(messagesRealm);
        }
        adapter.notifyDataSetChanged();
        chatRecycler.smoothScrollToPosition(messagesRealm.size()-1);
    }

    @Override
    public void resetInputText() {
        mListener.resetUserInputSales();
    }

    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface OnSalesChatFragmentInteractionListener {
        void resetUserInputSales();
    }
}
