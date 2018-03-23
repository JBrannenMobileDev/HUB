package jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.view_layer.custom_views.AdjustableScrollSpeedLayoutManager;
import jjpartnership.hub.view_layer.custom_views.HideShowScrollListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSalesChatFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SalesAgentsFragment extends Fragment implements SalesAgentView{
    @BindView(R.id.chat_empty_state)TextView emptyStateMessage;
    @BindView(R.id.chat_recycler_view)RecyclerView chatRecycler;
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.new_message_alert)TextView newMessageAlert;
    @BindView(R.id.currently_typing_tv)TextView currentlyTypingTv;

    private OnSalesChatFragmentInteractionListener mListener;
    private SalesAgentPresenter presenter;
    private AdjustableScrollSpeedLayoutManager layoutManager;
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
        layoutManager = new AdjustableScrollSpeedLayoutManager(getActivity().getApplicationContext());
        layoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(layoutManager);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager.setSpeedInMilliSeconds(55);
                chatRecycler.smoothScrollToPosition(chatRecycler.getAdapter().getItemCount()-1);
            }
        });


        chatRecycler.addOnScrollListener(
                new HideShowScrollListener() {
                    @Override
                    public void onHide() {
                        fab.hide();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fab.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                fab.setImageTintList(getResources().getColorStateList(R.color.grey_text));
                            }
                        }, 300);
                        animateHideNewMessageAlert();
                    }

                    @Override
                    public void onShow() {
                        newMessageAlert.setVisibility(View.GONE);
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                        fab.setImageTintList(getResources().getColorStateList(R.color.grey_text));
                        fab.show();
                    }
                });
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

    private void initCallback() {
        messageSelectedCallback = new BaseCallback<MessageRealm>() {
            @Override
            public void onResponse(MessageRealm selectedMessage) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void onReceiveMessages(RealmResults<MessageRealm> messagesRealm, HashMap<String, Long> userColorMap, boolean isCurrentUserMessage) {
        if(messagesRealm.size() > 0){
            emptyStateMessage.setVisibility(View.GONE);
        }else {
            emptyStateMessage.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            adapter = new SalesAgentRecyclerAdapter(getActivity().getApplicationContext(), messagesRealm,  messageSelectedCallback, userColorMap);
            chatRecycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        if(!fab.isShown()) {
            layoutManager.setSpeedInMilliSeconds(250);
            if(messagesRealm.size() > 0) chatRecycler.smoothScrollToPosition(messagesRealm.size() - 1);
        }else if(!isCurrentUserMessage){
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            fab.setImageTintList(getResources().getColorStateList(R.color.white));
            animateShowNewMessageAlert();
        }
    }

    private void animateShowNewMessageAlert() {
        newMessageAlert.animate().scaleX(0f).setDuration(0);
        newMessageAlert.setVisibility(View.VISIBLE);
        newMessageAlert.animate().scaleX(1f).setDuration(150);
    }

    private void animateHideNewMessageAlert() {
        newMessageAlert.animate().scaleX(0f).setDuration(150);
        newMessageAlert.setVisibility(View.GONE);
    }

    private void animateShowCurrentlyTypingTv(){
        currentlyTypingTv.animate().translationY(DpUtil.pxFromDp(getActivity().getApplicationContext(),25)).setDuration(0);
        currentlyTypingTv.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentlyTypingTv.animate().translationY(0f).setDuration(100);
            }
        }, 100);
    }

    private void animateHideCurrentlyTypingTv(){
        currentlyTypingTv.animate().translationY(DpUtil.pxFromDp(getActivity().getApplicationContext(),25)).setDuration(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentlyTypingTv.setVisibility(View.GONE);            }
        }, 100);
    }

    @Override
    public void resetInputText() {
        mListener.resetUserInputSales();
    }

    @Override
    public void onCurrentlyTypingUpdated(String nameToDisplay) {
        if(currentlyTypingTv.isShown()){
            if(nameToDisplay == null){
                animateHideCurrentlyTypingTv();
                currentlyTypingTv.setText("");
                presenter.updateCurrentlyTypingName("");
            }else {
                animateHideCurrentlyTypingTv();
                currentlyTypingTv.setText(nameToDisplay + " is typing...");
                presenter.updateCurrentlyTypingName(nameToDisplay);
                animateShowCurrentlyTypingTv();
            }
        }else{
            if(nameToDisplay != null) {
                currentlyTypingTv.setText(nameToDisplay + " is typing...");
                presenter.updateCurrentlyTypingName(nameToDisplay);
                animateShowCurrentlyTypingTv();
            }
        }
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
