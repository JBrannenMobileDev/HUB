package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.ActionBarUtil;
import mabbas007.tagsedittext.TagsEditText;

public class ShareLeadActivity extends AppCompatActivity implements SelectAccountFragment.OnFragmentInteractionListener,
        SelectUsersFragment.OnFragmentInteractionListener, TagsEditText.TagsEditListener{

    private static final String SELECT_USER = "Select Users";
    private static final String SELECT_ACCOUNT = "Select Account";

    @BindView(R.id.back_to_select_account_bt)ImageView backBt;
    @BindView(R.id.forward_to_select_account_bt)ImageView forwardBt;
    @BindView(R.id.title_tv)TextView title;
    @BindView(R.id.share_lead_view_pager)ViewPager viewPager;
    @BindView(R.id.new_group_message_tags)TagsEditText tags;
    @BindView(R.id.selected_account_name)TextView selectedAccountName;

    private ShareLeadAdapter pagerAdapter;
    private ArrayList<String> agentNames;
    private Map<String, String> selectedAgentIds;
    private String selectedAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lead);
        ButterKnife.bind(this);
        agentNames = new ArrayList<>();
        selectedAgentIds = new HashMap<>();
        initActionBar();
        initpager();
        initTagsView();
    }

    private void initpager() {
        pagerAdapter = new ShareLeadAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SelectAccountFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        animateTitleTextChange(SELECT_ACCOUNT);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        animateTitleTextChange(SELECT_ACCOUNT);
                        forwardBt.setVisibility(View.VISIBLE);
                        backBt.setVisibility(View.GONE);
                        break;
                    case 1:
                        animateTitleTextChange(SELECT_USER);
                        backBt.setVisibility(View.VISIBLE);
                        if(pagerAdapter.getCount() < 3) forwardBt.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initActionBar() {
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentDark, true, "Share Lead");
    }

    private void initTagsView() {
        tags.setTagsListener(this);
        tags.setTagsWithSpacesEnabled(true);
        tags.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, agentNames));
        tags.setThreshold(1);
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
    public void onAccountSelected(AccountRowItem account) {
        selectedAccountId = account.getAccountIdFire();
        pagerAdapter.addFragment(new SelectUsersFragment());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(1);
        backBt.setVisibility(View.VISIBLE);
        forwardBt.setVisibility(View.GONE);
        selectedAccountName.setText(account.getAccountName());
    }

    private void animateTitleTextChange(final String text){
        title.animate().scaleX(.0f).setDuration(150);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                title.animate().scaleX(1f).setDuration(150);
                title.setText(text);
            }
        }, 150);
    }

    @OnClick(R.id.back_to_select_account_bt)
    public void onBackToAccountClicked(){
        forwardBt.setVisibility(View.VISIBLE);
        backBt.setVisibility(View.GONE);
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.forward_to_select_account_bt)
    public void onForwardToUserSelectionClicked(){
        viewPager.setCurrentItem(1);
        backBt.setVisibility(View.VISIBLE);
        if(pagerAdapter.getCount() < 3) forwardBt.setVisibility(View.GONE);
    }

    @Override
    public void onTagsChanged(Collection<String> collection) {

    }

    @Override
    public void onEditingFinished() {

    }

    @Override
    public void onUserChecboxClicked(UserRealm user, boolean checked) {
        if(checked) {
            selectedAgentIds.put(user.getUid(), user.getUid());
            tags.setTag(Integer.valueOf(user.getPhoneNumber()), user.getFirstName() + " " + user.getLastName());
        }else{
            selectedAgentIds.remove(user.getUid());
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    private class ShareLeadAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        private void addFragment(Fragment fragment){
            fragments.add(fragment);
        }


        public ShareLeadAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
