package jjpartnership.hub.view_layer.activities.search_activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.account_activity.AccountChatActivity;
import jjpartnership.hub.view_layer.activities.direct_message_activity.DirectMessageActivity;
import jjpartnership.hub.view_layer.activities.main_activity.AccountRecyclerAdapter;
import jjpartnership.hub.view_layer.activities.main_activity.DirectMessageRecyclerAdapter;
import jjpartnership.hub.view_layer.activities.user_profile_activity.UserProfileActivity;
import jjpartnership.hub.view_layer.custom_views.BackAwareEditText;

public class NewDirectMessageActivity extends AppCompatActivity{

    @BindView(R.id.user_search_result_recycler)RecyclerView resultsRecycler;
    @BindView(R.id.title_frame_layout)FrameLayout titleLayout;
    @BindView(R.id.backAwareEditText)BackAwareEditText editTextBackAware;
    @BindView(R.id.scroll_view)NestedScrollView result;

    private BaseCallback<UserRealm> userSelectedCallback;
    private BaseCallback<UserRealm> messageUserSelectedCallback;
    private UserSearchResultsRecyclerAdapter usersAdapter;
    private BackAwareEditText.BackPressedListener backPressedListenerPhone;
    private RealmResults<UserRealm> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_direct_message);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentDark, true, "New Direct Message");
        ButterKnife.bind(this);
        initSearchView();
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm item) {
                launchUserProfileIntent(item);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        messageUserSelectedCallback = new BaseCallback<UserRealm>() {
            @Override
            public void onResponse(UserRealm toUser) {
                launchDirectMessageIntent(UserPreferences.getInstance().getUid(), toUser.getUid());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        allUsers = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).findAllAsync();
    }

    private void launchDirectMessageIntent(String uid, String toUid){
        Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
        directMessageIntent.putExtra("uid", uid);
        directMessageIntent.putExtra("toUid", toUid);
        startActivity(directMessageIntent);
        finish();
    }

    private void initSearchView() {
        backPressedListenerPhone = new BackAwareEditText.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareEditText editText) {
                editTextBackAware.clearFocus();
            }
        };
        editTextBackAware.setBackPressedListener(backPressedListenerPhone);

        editTextBackAware.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                if(newText.length() > 1) {
                    onUserInputChanged(newText.toString());
                }else{
                    onResultsReceived(new ArrayList<UserRealm>(), "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onUserInputChanged(String query) {
        if(allUsers == null){
            allUsers = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).sort("firstName").findAll();
        }

        List<UserRealm> userSearchResults = new ArrayList<>();
        for(UserRealm user : allUsers){
            if((user.getFirstName() + " " + user.getLastName()).toLowerCase().contains(query.toLowerCase())){
                userSearchResults.add(user);
            }
        }

        onResultsReceived(userSearchResults, query);
    }

    public void onResultsReceived(List<UserRealm> users, String userInput){
        if(users != null) {
            if (usersAdapter == null) {
                usersAdapter = new UserSearchResultsRecyclerAdapter(getApplicationContext(), users,
                        userSelectedCallback, messageUserSelectedCallback, null);
                resultsRecycler.setAdapter(usersAdapter);
            } else {
                usersAdapter.OnDataSetChanged(users);
            }
            usersAdapter.notifyDataSetChanged();
            if(users.size() == 0 && userInput != null && !userInput.isEmpty()){
                animateHideTitle();
                result.setVisibility(View.GONE);
            }else if(users.size() > 0){
                animateShowTitle();
                result.setVisibility(View.VISIBLE);
            }else{
                animateHideTitle();
                result.setVisibility(View.GONE);
            }
        }else{
            animateHideTitle();
            result.setVisibility(View.GONE);
        }
    }

    private void animateShowTitle() {
        if(titleLayout.getVisibility() == View.GONE) {
            titleLayout.animate().translationY(DpUtil.pxFromDp(getApplicationContext(), -48)).setDuration(0);
            titleLayout.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    titleLayout.animate().translationY(0f).setDuration(100);
                }
            }, 100);
        }
    }

    private void animateHideTitle() {
        titleLayout.animate().translationY(DpUtil.pxFromDp(getApplicationContext(),-48)).setDuration(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                titleLayout.setVisibility(View.GONE);    
            }
        }, 100);
    }

    private void launchUserProfileIntent(UserRealm rowItem){
        Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
        profileIntent.putExtra("userId", rowItem.getUid());
        startActivity(profileIntent);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
