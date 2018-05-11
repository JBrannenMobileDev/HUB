package jjpartnership.hub.view_layer.activities.main_activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.AccountRealm;
import jjpartnership.hub.data_layer.data_models.DirectChatRealm;
import jjpartnership.hub.data_layer.data_models.DirectItem;
import jjpartnership.hub.data_layer.data_models.GroupChatRealm;
import jjpartnership.hub.data_layer.data_models.RowItem;
import jjpartnership.hub.data_layer.data_models.MainAccountsModel;
import jjpartnership.hub.data_layer.data_models.MainDirectMessagesModel;
import jjpartnership.hub.data_layer.data_models.MainRecentModel;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.account_activity.AccountChatActivity;
import jjpartnership.hub.view_layer.activities.boot_activity.BootActivity;
import jjpartnership.hub.view_layer.activities.direct_message_activity.DirectMessageActivity;
import jjpartnership.hub.view_layer.activities.group_chat_activity.GroupChatActivity;
import jjpartnership.hub.view_layer.custom_views.BackAwareSearchView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {
    @BindView(R.id.search_selected_overlay)ImageView overlayImage;
    @BindView(R.id.search_results_layout)LinearLayout searchResultsLayout;
    @BindView(R.id.search_results_pager)ViewPager searchResultsPager;
    @BindView(R.id.search_results_tabs)TabLayout searchResultsTabs;
    @BindView(R.id.recent_list_view)RecyclerView recentRecyclerView;
    @BindView(R.id.recent_title_tv)TextView recentTitle;
    @BindView(R.id.accounts_list_view)RecyclerView accountsRecyclerView;
    @BindView(R.id.direct_messages_list_view)RecyclerView directMessagesRecyclerView;
    @BindView(R.id.group_messages_recycler_view)RecyclerView groupMessagesRecyclerView;
    @BindView(R.id.recent_empty_state_layout)LinearLayout recent_empty_layout;
    @BindView(R.id.accounts_empty_state_layout)LinearLayout accounts_empty_layout;
    @BindView(R.id.direc_messages_empty_state_layout)LinearLayout direct_messages_empty_state;
    @BindView(R.id.group_messages_empty_state_layout)LinearLayout group_messages_empty_state;
    @BindView(R.id.main_scrollview)NestedScrollView scrollView;

    private boolean searchResultsVisible;
    private Animation slideUpAnimation, slideDownAnimation, enterFromRightAnimation, exitToRightAnimation;
    private MainPresenter presenter;
    private AccountRecyclerAdapter accountsAdapter;
    private RecentRecyclerAdapter recentRecyclerAdapter;
    private DirectMessageRecyclerAdapter directRecyclerAdapter;
    private GroupMessagesRecyclerAdapter groupRecyclerAdapter;
    private BaseCallback<RowItem> accountSelectedCallback;
    private BaseCallback<RowItem> recentSelectedCallback;
    private BaseCallback<DirectItem> directMessageSelectedCallback;
    private BaseCallback<GroupChatRealm> groupMessageSelectedCallback;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.main_title));
        setSupportActionBar(toolbar);
        mAppBarLayout=findViewById(R.id.mAppBarLayout);
        mAppBarLayout.setElevation(0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorMainBg));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        directMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        groupMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAnimations();
        initAdapters();
        initScrollViewListener();
        presenter = new MainPresenterImp(this);
    }

    private void initScrollViewListener() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollView.canScrollVertically(-1)){
                    setToolbarElevation(4);
                }else{
                    setToolbarElevation(0);
                }
            }
        });
    }

    @Override
    public void setToolbarElevation(float height){
        mAppBarLayout.setElevation(DpUtil.pxFromDp(this, height));
    }

    @Override
    public void onDestroy(){
        presenter.onDestroy();
        super.onDestroy();
    }

    private void initAdapters() {
        accountSelectedCallback = new BaseCallback<RowItem>() {
            @Override
            public void onResponse(RowItem rowItem) {
                Intent accountChatIntent = new Intent(getApplicationContext(), AccountChatActivity.class);
                accountChatIntent.putExtra("account_id", rowItem.getAccountId());
                accountChatIntent.putExtra("account_name", rowItem.getAccountName());
                startActivity(accountChatIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        recentSelectedCallback = new BaseCallback<RowItem>() {
            @Override
            public void onResponse(RowItem rowItem) {
                if(rowItem.getItemType().equals(RowItem.TYPE_ACCOUNT)) {
                    Intent groupChatIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
                    groupChatIntent.putExtra("chatId", rowItem.getChatId());
                    startActivity(groupChatIntent);
                }else{
                    Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
                    DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", rowItem.getAccountId()).findFirst();
                    if(dChat != null) directMessageIntent.putExtra("toUid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid()));
                    directMessageIntent.putExtra("uid", UserPreferences.getInstance().getUid());
                    startActivity(directMessageIntent);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        directMessageSelectedCallback = new BaseCallback<DirectItem>() {
            @Override
            public void onResponse(DirectItem directItem) {
                Intent directMessageIntent = new Intent(getApplicationContext(), DirectMessageActivity.class);
                DirectChatRealm dChat = RealmUISingleton.getInstance().getRealmInstance().where(DirectChatRealm.class).equalTo("chatId", directItem.getDirectChatId()).findFirst();
                if(dChat != null) directMessageIntent.putExtra("toUid", dChat.getDirectChatUid(UserPreferences.getInstance().getUid()));
                directMessageIntent.putExtra("uid", UserPreferences.getInstance().getUid());
                startActivity(directMessageIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        groupMessageSelectedCallback = new BaseCallback<GroupChatRealm>() {
            @Override
            public void onResponse(GroupChatRealm chat) {
                Intent groupChatIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("chatId", chat.getChatId());
                startActivity(groupChatIntent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
        directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), new MainDirectMessagesModel(new RealmList<DirectItem>()), directMessageSelectedCallback);
        groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), new ArrayList<GroupChatRealm>(), groupMessageSelectedCallback);
        recentRecyclerView.setAdapter(recentRecyclerAdapter);
        directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
        groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.add_account_iv)
    public void onAddAccountClicked(){

    }

    @OnClick(R.id.add_direct_message_iv)
    public void onNewDirectMessageClicked(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        Drawable drawable = searchItem.getIcon();
        if(drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }        BackAwareSearchView searchView = (BackAwareSearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        initSearchView(searchView);
        return true;
    }

    private void initializeViewPager() {
//        if(null != tabLayout)
//            tabLayout.setupWithViewPager(mPager, true);
//
//        SplashScreenAdapter pagerAdapter = new SplashScreenAdapter(getSupportFragmentManager());
//        mPager.setAdapter(pagerAdapter);
    }

    private void initAnimations() {
        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_animation);
        enterFromRightAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        exitToRightAnimation = AnimationUtils.loadAnimation(this, R.anim.exit_to_right);
    }

    private void initSearchView(BackAwareSearchView searchView) {
        BackAwareSearchView.BackPressedListener backPressedListenerPhone = new BackAwareSearchView.BackPressedListener() {
            @Override
            public void onImeBack(BackAwareSearchView searchView) {
                searchView.clearFocus();
                if(searchView.getQuery().length() == 0){
                    searchResultsVisible = false;
                    overlayImage.setVisibility(View.GONE);
                    searchResultsLayout.startAnimation(slideDownAnimation);
                    searchResultsLayout.setVisibility(View.GONE);
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                }
            }
        };
        searchView.setBackPressedListener(backPressedListenerPhone);

        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchResultsVisible = false;
                overlayImage.setVisibility(View.GONE);
                searchResultsLayout.startAnimation(slideDownAnimation);
                searchResultsLayout.setVisibility(View.GONE);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlayImage.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is when user is done typing and clicks search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                toggleSearchResultsAnimation(newText);
                presenter.onSearchQuery(newText);
                return false;
            }
        });
    }

    private void toggleSearchResultsAnimation(String query){
        if(query.length() > 0 && !searchResultsVisible){
            searchResultsLayout.setVisibility(View.VISIBLE);
            searchResultsLayout.startAnimation(slideUpAnimation);
            searchResultsVisible = true;
        }else if(query.length() == 0 && searchResultsVisible){
            searchResultsLayout.startAnimation(slideDownAnimation);
            searchResultsLayout.setVisibility(View.GONE);
            searchResultsVisible = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.app_bar_search){
            overlayImage.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();
            DataManager.getInstance().clearRealmData();
            startActivity(new Intent(getApplicationContext(), BootActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRecentModelReceived(MainRecentModel recentModel) {
        if(recentModel.getRowItems() != null && recentModel.getRowItems().size() > 0){
            recent_empty_layout.setVisibility(View.GONE);
            if(recentRecyclerAdapter == null){
                recentRecyclerAdapter = new RecentRecyclerAdapter(getApplicationContext(), new MainRecentModel(new RealmList<RowItem>()), recentSelectedCallback);
                recentRecyclerView.setAdapter(recentRecyclerAdapter);
            }else{
                recentRecyclerAdapter.onDataSetChanged(recentModel);
            }
            recentRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAccountModelReceived(MainAccountsModel dataModel) {
        if(dataModel != null && dataModel.getRowItems() != null && dataModel.getRowItems().size() > 0) {
            accounts_empty_layout.setVisibility(View.GONE);
            if (accountsAdapter == null) {
                accountsAdapter = new AccountRecyclerAdapter(getApplicationContext(), dataModel, accountSelectedCallback);
                accountsRecyclerView.setAdapter(accountsAdapter);
            }
            accountsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDirectMessagesModelReceived(MainDirectMessagesModel dataModel) {
        if(dataModel != null && dataModel.getDirectItems() != null && dataModel.getDirectItems().size() > 0) {
            direct_messages_empty_state.setVisibility(View.GONE);
            directRecyclerAdapter = new DirectMessageRecyclerAdapter(getApplicationContext(), dataModel, directMessageSelectedCallback);
            directMessagesRecyclerView.setAdapter(directRecyclerAdapter);
            directRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGroupMessagesReceived(List<GroupChatRealm> groupChats){
        if(groupChats != null && groupChats.size() > 0){
            group_messages_empty_state.setVisibility(View.GONE);
            groupRecyclerAdapter = new GroupMessagesRecyclerAdapter(getApplicationContext(), groupChats, groupMessageSelectedCallback);
            groupMessagesRecyclerView.setAdapter(groupRecyclerAdapter);
            groupRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setWelcomeMessage(String typeCustomer) {

    }

    @Override
    public void setPageTitle(String title) {
        setTitle(title);
    }
}
