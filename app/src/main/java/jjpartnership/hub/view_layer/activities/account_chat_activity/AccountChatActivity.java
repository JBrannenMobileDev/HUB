package jjpartnership.hub.view_layer.activities.account_chat_activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;

public class AccountChatActivity extends AppCompatActivity implements SalesAgentsFragment.OnSalesChatFragmentInteractionListener,
        CustomerChatActivity.OnCustomerChatInteractionListener{
    @BindView(R.id.pager)ViewPager pager;
    @BindView(R.id.tabs)TabLayout tabLayout;

    private ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_chat);
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0);
        setTitle(getIntent().getStringExtra("account_name"));
        accountId = getIntent().getStringExtra("account_id");
        adapter.addFragment(new SalesAgentsFragment(), "Sales Team");
        adapter.addFragment(new CustomerChatActivity(), "Account");
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
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
