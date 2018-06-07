package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.view_layer.activities.main_activity.AccountSearchResultsFragment;
import jjpartnership.hub.view_layer.activities.main_activity.SharedLeadsSearchResultFragment;
import jjpartnership.hub.view_layer.activities.main_activity.UsersSearchResultFragment;
import mabbas007.tagsedittext.TagsEditText;

public class ShareLeadActivity extends AppCompatActivity implements SelectAccountFragment.OnFragmentInteractionListener,
        SelectUsersFragment.OnFragmentInteractionListener, TagsEditText.TagsEditListener{
    @BindView(R.id.back_to_select_account_bt)ImageView backBt;
    @BindView(R.id.forward_to_select_account_bt)ImageView forwardBt;
    @BindView(R.id.title_tv)TextView title;
    @BindView(R.id.share_lead_view_pager)ViewPager viewPager;
    @BindView(R.id.new_group_message_tags)TagsEditText tags;
    @BindView(R.id.selected_account_name)TextView selectedAccountName;

    private ShareLeadAdapter pagerAdapter;
    private ArrayList<String> agentNames;
    private ArrayList<String> selectedAgentNames;
    private Map<String, UserRealm> agents;
    private List<String> selectedAgentIds;
    private String selectedAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lead);
        ButterKnife.bind(this);
        agentNames = new ArrayList<>();
        agents = new HashMap<>();
        selectedAgentIds = new ArrayList<>();
        selectedAgentNames = new ArrayList<>();
        initActionBar();
        initpager();
        initTagsView();
    }

    private void initpager() {
        pagerAdapter = new ShareLeadAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SelectAccountFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void initActionBar() {
        ActionBarUtil.initActionBar(this, R.color.colorAccentVeryDark, 8,
                R.color.colorAccentVeryDark, true, "Share Lead");
    }

    private void initTagsView() {
        tags.setTagsListener(this);
        tags.setTagsWithSpacesEnabled(true);
        tags.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, agentNames));
        tags.setThreshold(1);
        String[] selectedAgentsArray = new String[selectedAgentNames.size()];
        selectedAgentsArray = selectedAgentNames.toArray(selectedAgentsArray);
        tags.setTags(selectedAgentsArray);

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
        title.setText("-Select Users-");
    }

    @OnClick(R.id.back_to_select_account_bt)
    public void onBackToAccountClicked(){
        title.setText("-Select Account-");
        forwardBt.setVisibility(View.VISIBLE);
        backBt.setVisibility(View.GONE);
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.forward_to_select_account_bt)
    public void onForwardToUserSelectionClicked(){
        viewPager.setCurrentItem(1);
        backBt.setVisibility(View.VISIBLE);
        if(pagerAdapter.getCount() < 3) forwardBt.setVisibility(View.GONE);
        title.setText("-Select Users-");
    }

    @Override
    public void onUserSelected(List<UserRealm> users) {

    }

    @Override
    public void onTagsChanged(Collection<String> collection) {

    }

    @Override
    public void onEditingFinished() {

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
