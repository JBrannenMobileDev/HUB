package jjpartnership.hub.view_layer.activities.account_activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.view_layer.activities.account_activity.account_details_fragment.AccountDetailsFragment;
import jjpartnership.hub.view_layer.activities.account_activity.customer_requests_fragment.CustomerRequestsFragment;
import jjpartnership.hub.view_layer.activities.account_activity.sales_agent_fragment.SalesAgentsFragment;

public class AccountChatActivity extends AppCompatActivity implements SalesAgentsFragment.OnSalesChatFragmentInteractionListener,
        CustomerRequestsFragment.OnFragmentInteractionListener,
        AccountChatView, AccountDetailsFragment.OnAccountDetailsInteractionListener{
    @BindView(R.id.pager)ViewPager pager;
    @BindView(R.id.tabs)TabLayout tabLayout;

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
        adapter.addFragment(salesAgentFragment, "Group Messages");
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

    }

    private void initListeners() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
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
