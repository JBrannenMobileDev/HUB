package jjpartnership.hub.view_layer.activities.direct_message_activity;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.MessageRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.CommunicationsUtil;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment.SalesAgentRecyclerAdapter;
import jjpartnership.hub.view_layer.custom_views.AdjustableScrollSpeedLayoutManager;
import jjpartnership.hub.view_layer.custom_views.BackAwareAutofillMultiLineEditText;
import jjpartnership.hub.view_layer.custom_views.HideShowScrollListener;

public class DirectMessageActivity extends AppCompatActivity implements DirectMessageView, BackAwareAutofillMultiLineEditText.BackPressedListener{
    @BindView(R.id.user_infor_linear_layout)LinearLayout userInfoLayout;
    @BindView(R.id.send_image_view)ImageView sendImage;
    @BindView(R.id.user_message_et)BackAwareAutofillMultiLineEditText userInput;
    @BindView(R.id.new_message_alert)TextView newMessageAlert;
    @BindView(R.id.currently_typing_tv)TextView currentlyTypingTv;
    @BindView(R.id.chat_recycler_view)RecyclerView chatRecycler;
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.chat_empty_state)TextView emptyStateMessage;

    private DirectMessagePresenter presenter;
    private AdjustableScrollSpeedLayoutManager layoutManager;
    private SalesAgentRecyclerAdapter adapter;
    private BaseCallback<MessageRealm> messageSelectedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutManager = new AdjustableScrollSpeedLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(layoutManager);
        initListeners();
        userInput.setBackPressedListener(this);
        presenter = new DirectMessagePresenterImp(this, getIntent().getStringExtra("uid"),
                getIntent().getBooleanExtra("showUserInfo", false));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @OnClick(R.id.send_image_view)
    public void onSendClicked(){
        presenter.onSendMessageClicked();
        animateHideDetails();
        hideKeyboard();
    }

    @OnClick(R.id.direct_message_layout)
    public void onSendDirectMessageClicked(){
        userInfoLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.send_email_layout)
    public void onSendEmailClicked(){
        presenter.onSendEmailClicked();
    }

    @OnClick(R.id.call_layout)
    public void onCallClicked(){
        presenter.onCallClicked();
    }

    @Override
    public void onImeBack(BackAwareAutofillMultiLineEditText editText) {
        animateShowDetails();
    }

    private void animateShowDetails() {
        userInfoLayout.animate().translationY(DpUtil.pxFromDp(getApplicationContext(), 0)).setDuration(250);
    }

    private void animateHideDetails() {
        userInfoLayout.animate().translationY(DpUtil.pxFromDp(getApplicationContext(), -92)).setDuration(250);
    }

    private void initListeners() {
        userInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                animateHideDetails();
                return false;
            }
        });

        userInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    animateHideDetails();
                }
            }
        });

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    setSendImageColor(ContextCompat.getColor(getApplicationContext(), R.color.colorOrange));
                } else {
                    setSendImageColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryVeryLight));
                }
                presenter.onUserInputChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setSendImageColor(int color){
        sendImage.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void hideUserInfoLayout() {
        userInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void setActivityTitle(String userName) {
        setTitle(userName);
    }

    @Override
    public void onCurrentlyTypingUpdated(String nameToDisplay) {
        if (currentlyTypingTv.isShown()) {
            if(!currentlyTypingTv.getText().equals(nameToDisplay)) {
                if (nameToDisplay == null) {
                    animateHideCurrentlyTypingTv();
                    currentlyTypingTv.setText("");
                } else {
                    animateHideCurrentlyTypingTv();
                    currentlyTypingTv.setText(nameToDisplay + " is typing...");
                    animateShowCurrentlyTypingTv();
                }
            }
        } else {
            if (nameToDisplay != null) {
                currentlyTypingTv.setText(nameToDisplay + " is typing...");
                animateShowCurrentlyTypingTv();
            }
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
        currentlyTypingTv.animate().translationY(DpUtil.pxFromDp(getApplicationContext(),25)).setDuration(0);
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
        currentlyTypingTv.animate().translationY(DpUtil.pxFromDp(getApplicationContext(),25)).setDuration(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentlyTypingTv.setVisibility(View.GONE);            }
        }, 100);
    }

    @Override
    public void onReceiveMessages(RealmResults<MessageRealm> messagesRealm, HashMap<String, Long> usersColors, boolean isCurrentUserMessage) {
        if(messagesRealm.size() > 0){
            emptyStateMessage.setVisibility(View.GONE);
        }else {
            emptyStateMessage.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            adapter = new SalesAgentRecyclerAdapter(getApplicationContext(), messagesRealm,  messageSelectedCallback, usersColors);
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

    @Override
    public void onSendCallIntent(String phoneNumber) {
        CommunicationsUtil.launchPhoneCallIntent(phoneNumber, getApplicationContext());
    }

    @Override
    public void onSendEmailIntent(String email) {
        CommunicationsUtil.launchEmailIntent(email, getApplicationContext());
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
