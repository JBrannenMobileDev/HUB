package jjpartnership.hub.view_layer.activities.settings_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.ActionBarUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.boot_activity.BootActivity;
import jjpartnership.hub.view_layer.activities.user_profile_activity.UserProfileActivity;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.user_icon)TextView userIcon;
    @BindView(R.id.user_name)TextView userName;
    @BindView(R.id.user_email)TextView userEmail;
    @BindView(R.id.settings_header_linear_layout)LinearLayout headerLayout;

    private UserRealm currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ActionBarUtil.initActionBar(this, R.color.colorAccentDark, 0,
                R.color.colorAccentVeryDark, true, "Settings");
        fetchData();
    }

    private void fetchData() {
        currentUser = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class)
                .equalTo("uid", UserPreferences.getInstance().getUid()).findFirst();

        if(currentUser != null){
            initViewWithData();
        }
    }

    private void initViewWithData() {
        userName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        userIcon.setText(currentUser.getFirstName().substring(0,1));
        userEmail.setText(currentUser.getEmail());
        int iconColor = UserColorUtil.getUserColor(currentUser.getUserColor());
        int headerColor = UserColorUtil.getUserColorDark(currentUser.getUserColor());
        userIcon.setBackgroundTintList(getResources().getColorStateList(iconColor));
        headerLayout.setBackgroundTintList(getResources().getColorStateList(headerColor));
        ActionBarUtil.setStatusBarColor(this, headerColor);
        ActionBarUtil.setActionBarColor(this, headerColor);
    }

    @OnClick(R.id.edit_profile_tv)
    public void onEditProfileClicked(){
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class)
                .putExtra("userId", UserPreferences.getInstance().getUid()));
    }

    @OnClick(R.id.general_settings_sign_out_tv)
    public void onSignOutClicked(){
        FirebaseAuth.getInstance().signOut();
        DataManager.getInstance().clearRealmData();
        startActivity(new Intent(getApplicationContext(), BootActivity.class));
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
}
