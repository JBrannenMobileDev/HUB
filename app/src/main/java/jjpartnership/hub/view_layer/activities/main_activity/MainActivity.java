package jjpartnership.hub.view_layer.activities.main_activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import jjpartnership.hub.R;
import jjpartnership.hub.view_layer.activities.boot_activity.BootActivity;
import jjpartnership.hub.view_layer.custom_views.BackAwareSearchView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView {
    @BindView(R.id.search_selected_overlay)ImageView overlayImage;
    @BindView(R.id.search_results_layout)LinearLayout searchResultsLayout;
    @BindView(R.id.search_results_pager)ViewPager searchResultsPager;
    @BindView(R.id.search_results_tabs)TabLayout searchResultsTabs;


    private boolean searchResultsVisible;
    private Animation slideUpAnimation, slideDownAnimation, enterFromRightAnimation, exitToRightAnimation;
    private MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presenter = new MainPresenterImp(this);
        initAnimations();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        BackAwareSearchView searchView = (BackAwareSearchView) searchItem.getActionView();
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
            startActivity(new Intent(getApplicationContext(), BootActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
