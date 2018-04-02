package jjpartnership.hub.view_layer.activities.account_chat_activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.view_layer.activities.account_chat_activity.account_details_fragment.AccountDetailsFragment;
import jjpartnership.hub.view_layer.activities.account_chat_activity.sales_agent_fragment.SalesAgentsFragment;
import jjpartnership.hub.view_layer.custom_views.BackAwareAutofillMultiLineEditText;

public class AccountChatActivity extends AppCompatActivity implements SalesAgentsFragment.OnSalesChatFragmentInteractionListener,
        BackAwareAutofillMultiLineEditText.BackPressedListener, CustomerRequestsFragment.OnFragmentInteractionListener,
        AccountChatView, AccountDetailsFragment.OnAccountDetailsInteractionListener{
    @BindView(R.id.pager)ViewPager pager;
    @BindView(R.id.tabs)TabLayout tabLayout;
    @BindView(R.id.send_image_view)ImageView sendImage;
    @BindView(R.id.user_input_layout)FrameLayout userInputLayout;
    @BindView(R.id.user_message_sales_team_et)BackAwareAutofillMultiLineEditText userInputSalesTeam;

    private ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    private String accountId;
    private int colorOrange;
    private int colorGrey;
    private SalesAgentsFragment salesAgentFragment;
    private CustomerRequestsFragment customerRequestsFragment;
    private AccountDetailsFragment accountDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_chat);
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0);
        setTitle(getIntent().getStringExtra("account_name"));
        accountId = getIntent().getStringExtra("account_id");
        Bundle bundle = new Bundle();
        bundle.putString("account_name", getIntent().getStringExtra("account_name"));
        bundle.putString("account_id", getIntent().getStringExtra("account_id"));
        salesAgentFragment = new SalesAgentsFragment();
        customerRequestsFragment = new CustomerRequestsFragment();
        accountDetailsFragment = new AccountDetailsFragment();
        salesAgentFragment.setArguments(bundle);
        customerRequestsFragment.setArguments(bundle);
        accountDetailsFragment.setArguments(bundle);
        adapter.addFragment(accountDetailsFragment, "Details");
        adapter.addFragment(salesAgentFragment, "Sales Team Chat");
        adapter.addFragment(customerRequestsFragment, "Customer Requests");
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        colorOrange = ContextCompat.getColor(getApplicationContext(), R.color.colorOrange);
        colorGrey = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryVeryLight);
        initListeners();
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
    }

    @OnClick(R.id.send_image_view)
    public void onSendClicked(){
        if(userInputSalesTeam.isShown()){
            salesAgentFragment.onSendMessageClicked();
        }
    }

    private void initListeners() {
        userInputSalesTeam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });

        userInputSalesTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    setSendImageColor(colorOrange);
                }else{
                    setSendImageColor(colorGrey);
                }
                salesAgentFragment.onUserInputChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setEditTextVisible(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        userInputSalesTeam.setBackPressedListener(this);
    }

    private void setEditTextVisible(int position) {
        switch(position){
            case 0:
                hideKeyboard();
                animateHideUserInputLayout();
                break;
            case 1:
                if(!userInputLayout.isShown()) animateShowUserInputLayout();
                hideKeyboard();
                userInputSalesTeam.setVisibility(View.VISIBLE);
                if (userInputSalesTeam.getText().length() > 0) {
                    setSendImageColor(colorOrange);
                } else {
                    setSendImageColor(colorGrey);
                }
                break;
            case 2:
                hideKeyboard();
                userInputLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void animateShowUserInputLayout(){
        userInputLayout.setVisibility(View.VISIBLE);
    }

    private void animateHideUserInputLayout(){
        userInputLayout.setVisibility(View.GONE);
    }

    private void setSendImageColor(int color){
        sendImage.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onImeBack(BackAwareAutofillMultiLineEditText editText) {

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public void resetUserInputSales() {
        userInputSalesTeam.setText("");
    }

    @Override
    public void onSetPagerPage(int position) {
        pager.setCurrentItem(position);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
