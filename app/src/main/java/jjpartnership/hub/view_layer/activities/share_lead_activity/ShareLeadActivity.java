package jjpartnership.hub.view_layer.activities.share_lead_activity;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRowItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import mabbas007.tagsedittext.TagsEditText;

public class ShareLeadActivity extends AppCompatActivity implements SelectAccountFragment.OnFragmentInteractionListener,
        SelectUsersFragment.OnFragmentInteractionListener, TagsEditText.TagsEditListener, ComposeMessageFragment.OnFragmentInteractionListener{

    @BindView(R.id.share_lead_view_pager)ViewPager viewPager;
    @BindView(R.id.new_group_message_tags)TagsEditText tags;
    @BindView(R.id.selected_account_name)TextView selectedAccountName;
    @BindView(R.id.share_lead_tabs)TabLayout tabLayout;
    @BindView(R.id.header_layout)LinearLayout header;

    private ShareLeadAdapter pagerAdapter;
    private ArrayList<String> agentNames;
    private String selectedAccountId;
    private ArrayList<String> selectedAgentNames;
    private ArrayList<String> selectedAgentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lead);
        ButterKnife.bind(this);
        agentNames = new ArrayList<>();
        selectedAgentNames = new ArrayList<>();
        selectedAgentIds = new ArrayList<>();
        initActionBar();
        initPager();
    }

    private void initPager() {
        pagerAdapter = new ShareLeadAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SelectAccountFragment(), "Select Account");
        pagerAdapter.addFragment(new SelectUsersFragment(), "Select Users");
        pagerAdapter.addFragment(new ComposeMessageFragment(), "Message");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        initTagsView();
        selectedAccountId = account.getAccountIdFire();
        pagerAdapter.updateAccountIdForUserFragment(account.getAccountIdFire());
        selectedAccountName.setText(account.getAccountName());
        viewPager.setCurrentItem(1);
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
            selectedAgentNames.add(user.getFirstName() + " " + user.getLastName());
            selectedAgentIds.add(user.getUid());
            String[] selectedAgentsArray = new String[selectedAgentNames.size()];
            selectedAgentsArray = selectedAgentNames.toArray(selectedAgentsArray);
            tags.setTags(selectedAgentsArray);
        }else{
            for(int i = 0; i < selectedAgentNames.size(); i++){
                if(selectedAgentNames.get(i).equalsIgnoreCase(user.getFirstName() + " " + user.getLastName())){
                    selectedAgentNames.remove(i);
                    selectedAgentIds.remove(i);
                }
            }
            String[] selectedAgentsArray = new String[selectedAgentNames.size()];
            selectedAgentsArray = selectedAgentNames.toArray(selectedAgentsArray);
            tags.setTags(selectedAgentsArray);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public void onSoftKeyboardStateChanged(boolean visible) {
        if(visible){
            header.setVisibility(View.GONE);
        }else{
            header.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSendLeadClicked(String message, String title) {
        if(selectedAccountId != null && selectedAgentIds != null && selectedAgentIds.size() > 0 && message != null && message.length() > 0){
            DataManager.getInstance().createNewGroupChat(selectedAgentIds, selectedAccountId, UserPreferences.getInstance().getUid(), title, message);
            GroupChatRealm createdChat = RealmUISingleton.getInstance().getRealmInstance().where(GroupChatRealm.class).equalTo("groupName", title).findFirst();
            if(createdChat != null) {
                Toast.makeText(this, "Group Created", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Unable to create Group. Check internet connection and try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ShareLeadAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> tabTitles;

        private void addFragment(Fragment fragment, String tabTitle){
            fragments.add(fragment);
            tabTitles.add(tabTitle);
        }

        public ShareLeadAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            tabTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void updateAccountIdForUserFragment(String accountIdFire) {
            ((SelectUsersFragment)fragments.get(1)).updateAccountId(accountIdFire);
        }
    }
}
