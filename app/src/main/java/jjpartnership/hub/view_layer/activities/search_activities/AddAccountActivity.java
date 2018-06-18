package jjpartnership.hub.view_layer.activities.search_activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.view_layer.activities.account_activity.AccountChatActivity;
import jjpartnership.hub.view_layer.activities.add_new_account_activity.AddNewAccountActivity;
import jjpartnership.hub.view_layer.activities.main_activity.AccountRecyclerAdapter;
import jjpartnership.hub.view_layer.custom_views.BackAwareEditText;

public class AddAccountActivity extends AppCompatActivity{

    @BindView(R.id.account_search_result_recycler)RecyclerView resultsRecycler;
    @BindView(R.id.search_results_empty_frame_layout)FrameLayout noResultsLayout;
    @BindView(R.id.no_results_user_input_tv)TextView noResultsUserInputText;
    @BindView(R.id.title_frame_layout)FrameLayout titleLayout;
    @BindView(R.id.backAwareEditText)BackAwareEditText editTextBackAware;
    @BindView(R.id.scroll_view)NestedScrollView result;

    private BaseCallback<AccountRowItem> accountSelectedCallback;
    private AccountRecyclerAdapter accountsAdapter;
    private BackAwareEditText.BackPressedListener backPressedListenerPhone;
    private RealmResults<AccountRealm> allUserCompanyAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentDark, true, "Add Account");
        ButterKnife.bind(this);
        initSearchView();
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        accountSelectedCallback = new BaseCallback<AccountRowItem>() {
            @Override
            public void onResponse(AccountRowItem item) {
                launchAccountDetailsIntent(item);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        allUserCompanyAccounts = RealmUISingleton.getInstance().getRealmInstance().where(AccountRealm.class).findAllAsync();
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
                    onResultsReceived(new ArrayList<AccountRowItem>(), "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onUserInputChanged(String query) {
        if(allUserCompanyAccounts == null){
            allUserCompanyAccounts = RealmUISingleton.getInstance().getRealmInstance().where(AccountRealm.class).findAll();
        }
        
        List<AccountRowItem> accountSearchResults = new ArrayList<>();
        for(AccountRealm item : allUserCompanyAccounts){
            CompanyRealm company = RealmUISingleton.getInstance().getRealmInstance().where(CompanyRealm.class).equalTo("companyId", item.getCompanyCustomerId()).findFirst();
            if(company != null) {
                if (company.getName().toLowerCase().contains(query.toLowerCase())) {
                    AccountRowItem newItem = new AccountRowItem();
                    newItem.setAccountIdFire(item.getAccountIdFire());
                    newItem.setAccountName(company.getName());
                    accountSearchResults.add(newItem);
                }
            }
        }
        Collections.sort(accountSearchResults);
        onResultsReceived(accountSearchResults, query);
    }

    @OnClick(R.id.request_new_account_tv)
    public void onNewAccountClicked(){
        startActivity(new Intent(getApplicationContext(), AddNewAccountActivity.class));
    }

    public void onResultsReceived(List<AccountRowItem> items, String userInput){
        if(items != null) {
            noResultsLayout.setVisibility(View.GONE);
            if (accountsAdapter == null) {
                accountsAdapter = new AccountRecyclerAdapter(getApplicationContext(), items, accountSelectedCallback);
                resultsRecycler.setAdapter(accountsAdapter);
            } else {
                accountsAdapter.OnDataSetChanged(items);
            }
            accountsAdapter.notifyDataSetChanged();
            if(items.size() == 0 && userInput != null && !userInput.isEmpty()){
                noResultsLayout.setVisibility(View.VISIBLE);
                noResultsUserInputText.setText(userInput);
                animateHideTitle();
                result.setVisibility(View.GONE);
            }else if(items.size() > 0){
                animateShowTitle();
                result.setVisibility(View.VISIBLE);
            }else{
                animateHideTitle();
                result.setVisibility(View.GONE);
            }
        }else{
            animateHideTitle();
            result.setVisibility(View.GONE);
            if(userInput != null && !userInput.isEmpty()) {
                noResultsLayout.setVisibility(View.VISIBLE);
                noResultsUserInputText.setText(userInput);
            }else{
                noResultsLayout.setVisibility(View.GONE);
            }
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

    private void launchAccountDetailsIntent(AccountRowItem rowItem){
        Intent accountChatIntent = new Intent(getApplicationContext(), AccountChatActivity.class);
        accountChatIntent.putExtra("account_id", rowItem.getAccountIdFire());
        accountChatIntent.putExtra("account_name", rowItem.getAccountName());
        startActivity(accountChatIntent);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
