package jjpartnership.hub.view_layer.activities.user_profile_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.UserColorUtil;
import jjpartnership.hub.utils.UserPreferences;

public class UserProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_icon_tv)TextView userIcon;
    @BindView(R.id.user_profile_name)TextView userName;
    @BindView(R.id.user_phone_tv)TextView phoneNumber;
    @BindView(R.id.user_email_tv)TextView userEmail;
    @BindView(R.id.info1_tv)TextView businessUnit;
    @BindView(R.id.info2_tv)TextView role;
    @BindView(R.id.user_profile_action_layout)LinearLayout actionLayout;
    @BindView(R.id.color_picker_layout)LinearLayout colorPickerLayout;
    @BindView(R.id.user_profile_header_frame_layout)FrameLayout headerColor;
    private String userId;
    private UserRealm user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userId = getIntent().getExtras().getString("userId");
        fetchData();
    }

    private void fetchData() {
        if(userId.equals(UserPreferences.getInstance().getUid())){
            actionLayout.setVisibility(View.GONE);
            colorPickerLayout.setVisibility(View.VISIBLE);
        }else{
            actionLayout.setVisibility(View.VISIBLE);
            colorPickerLayout.setVisibility(View.GONE);
        }

        user = RealmUISingleton.getInstance().getRealmInstance().where(UserRealm.class).equalTo("uid", userId).findFirst();
        if(user != null){
            setUserColor(user.getUserColor());
            userName.setText(user.getFirstName() + " " + user.getLastName());
            phoneNumber.setText("Phone: " + user.getPhoneNumber());
            userEmail.setText("Email: " + user.getEmail());
            businessUnit.setText("Business Unit: " + user.getBusinessUnit());
            role.setText("Role: " + user.getRole());
        }
    }

    private void setSelectedColor(int color){
        userIcon.setBackgroundTintList(getResources().getColorStateList(UserColorUtil.getUserColor(color)));
        headerColor.setBackgroundTintList(getResources().getColorStateList(UserColorUtil.getUserColorDark(color)));
    }

    private void setUserColor(int color){
        userIcon.setBackgroundTintList(getResources().getColorStateList(UserColorUtil.getUserColor(color)));
        headerColor.setBackgroundTintList(getResources().getColorStateList(UserColorUtil.getUserColorDark(color)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @OnClick(R.id.user_profile_send_email)
    public void onSendEmailClicked(){

    }

    @OnClick(R.id.user_profile_send_direct_message)
    public void onSendDirectMessageClicked(){

    }

    @OnClick(R.id.user_profile_call)
    public void onCallClicked(){

    }

    @OnClick(R.id.save_color_tv)
    public void onSaveColorClicked(){

    }

    @OnClick(R.id.cancel_color_tv)
    public void onCancelColorClicked(){
        if(user != null)
        setUserColor(user.getUserColor());
    }

    @OnClick(R.id.user_color_1)
    public void onColor1CLicked(){
        setSelectedColor(1);
    }

    @OnClick(R.id.user_color_2)
    public void onColor2CLicked(){
        setSelectedColor(2);
    }

    @OnClick(R.id.user_color_3)
    public void onColor3CLicked(){
        setSelectedColor(3);
    }

    @OnClick(R.id.user_color_4)
    public void onColor4CLicked(){
        setSelectedColor(4);
    }

    @OnClick(R.id.user_color_5)
    public void onColor5CLicked(){
        setSelectedColor(5);
    }

    @OnClick(R.id.user_color_6)
    public void onColor6CLicked(){
        setSelectedColor(6);
    }

    @OnClick(R.id.user_color_7)
    public void onColor7CLicked(){
        setSelectedColor(7);
    }

    @OnClick(R.id.user_color_8)
    public void onColor8CLicked(){
        setSelectedColor(8);
    }

    @OnClick(R.id.user_color_9)
    public void onColor9CLicked(){
        setSelectedColor(9);
    }

    @OnClick(R.id.user_color_10)
    public void onColor10CLicked(){
        setSelectedColor(10);
    }

    @OnClick(R.id.user_color_11)
    public void onColor11CLicked(){
        setSelectedColor(11);
    }

    @OnClick(R.id.user_color_12)
    public void onColor12CLicked(){
        setSelectedColor(12);
    }

    @OnClick(R.id.user_color_13)
    public void onColor13CLicked(){
        setSelectedColor(13);
    }

    @OnClick(R.id.user_color_14)
    public void onColor14CLicked(){
        setSelectedColor(14);
    }

    @OnClick(R.id.user_color_15)
    public void onColor15CLicked(){
        setSelectedColor(15);
    }
}
