package jjpartnership.hub.view_layer.activities.create_customer_account_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.view_layer.activities.boot_activity.BootActivity;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.AccountDetailsPagerAdapter;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.AccountDetailsPresenter;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.AccountDetailsPresenterImp;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.AccountDetailsView;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.UserInfoNamePagerFragment;
import jjpartnership.hub.view_layer.activities.create_agent_account_activity.UserPositionInfoPagerFragment;
import jjpartnership.hub.view_layer.activities.main_activity.MainActivity;
import jjpartnership.hub.view_layer.custom_views.NonSwipeableViewPager;

public class CustomerAccountDetailsActivity extends AppCompatActivity implements UserInfoNamePagerFragment.OnFragmentInteractionListener,
        UserPositionInfoPagerFragment.OnFragmentInteractionListener, AccountDetailsView{
    @BindView(R.id.account_details_pager)NonSwipeableViewPager pager;
    @BindView(R.id.submit_button)Button submitBt;
    @BindView(R.id.next_button)Button nextBt;
    @BindView(R.id.input_error_tv)TextView errorTv;
    @BindView(R.id.user_company_tv)TextView companyName;

    private AccountDetailsPagerAdapter adapter;
    private FirebaseAuth mAuth;
    private AccountDetailsPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        ButterKnife.bind(this);
        hideStatusBar();
        mAuth = FirebaseAuth.getInstance();
        initViewPager();
        presenter = new AccountDetailsPresenterImp(this);
    }

    @OnClick(R.id.sign_out_tv)
    public void onSignOutClicked(){
        signOut();
        startActivity(new Intent(getApplicationContext(), BootActivity.class));
    }

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        hideStatusBar();
    }

    @Override
    public void onBackPressed(){
        if(pager.getCurrentItem() == 1){
            pager.setCurrentItem(0);
            nextBt.setVisibility(View.VISIBLE);
            submitBt.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.next_button)
    public void onNextClicked(){
        presenter.onNextClicked();
    }

    @OnClick(R.id.submit_button)
    public void onSubmitClicked(){
        presenter.onSubmitClicked();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initViewPager() {
        adapter = new AccountDetailsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    private void launchMainActivity() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onFirstNameTextChanged(String firstName) {
        presenter.onFirstNameUpdated(firstName);
    }

    @Override
    public void onLastNameTextChanged(String lastName) {
        presenter.onLastNameUpdated(lastName);
    }

    @Override
    public void onPhoneNumberTextChanged(String phoneNumber) {
        presenter.onPhoneNumberUpdated(phoneNumber);
    }

    @Override
    public void onEtTouched() {

    }

    @Override
    public void onBackPressedFromEt() {

    }

    @Override
    public void onBusineesUnitInputChanged(String businessUnit) {
        presenter.onBusinessUnitUpdated(businessUnit);
    }

    @Override
    public void onRoleInputChanged(String role) {
        presenter.onRoleUpdated(role);
    }

    @Override
    public void onUserInputError(String s) {
        errorTv.setText(s);
    }

    @Override
    public void onNextView() {
        pager.setCurrentItem(1);
        nextBt.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);
        submitBt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFormComplete() {
        launchMainActivity();
    }

    @Override
    public void setCompanyName(String name) {
        companyName.setText(name);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
