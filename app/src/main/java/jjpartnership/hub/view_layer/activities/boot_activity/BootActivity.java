package jjpartnership.hub.view_layer.activities.boot_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuyenmonkey.mkloader.MKLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.RealmUISingleton;
import jjpartnership.hub.utils.StringValidationUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.create_customer_account_activity.CustomerAccountDetailsActivity;
import jjpartnership.hub.view_layer.activities.main_activity.MainActivity;
import jjpartnership.hub.view_layer.activities.unauthorized_user_activity.UnauthorizedUserActivity;
import jjpartnership.hub.view_layer.custom_views.BackAwareEditText;

public class BootActivity extends AppCompatActivity implements BackAwareEditText.BackPressedListener{

    private static final String TAG = "EmailPassword";

    @BindView(R.id.field_email)BackAwareEditText mEmailField;
    @BindView(R.id.field_password)BackAwareEditText mPasswordField;
    @BindView(R.id.sales_agent_tv) TextView salesAgentTv;
    @BindView(R.id.customer_tv) TextView customerTv;
    @BindView(R.id.account_type_layout)LinearLayout accountTypeLayout;
    @BindView(R.id.email_create_account_button)Button createAccountBt;
    @BindView(R.id.login_button)Button loginBt;
    @BindView(R.id.show_login_view_tv)TextView showLoginViewTv;
    @BindView(R.id.show_create_account_view_tv)TextView showCreateAccountTv;
    @BindView(R.id.account_type_tv)TextView accountTypeTitle;
    @BindView(R.id.boot_verification_email)TextView verificationTv;
    @BindView(R.id.loading_icon)MKLoader loadingIcon;
    @BindView(R.id.password_requirements_tv)TextView passwrodRequirments;
    @BindView(R.id.boot_title_tv)TextView bootTitle;
    @BindView(R.id.loading_frame_layout)FrameLayout bootLoadingLayout;
    @BindView(R.id.company_logo)LinearLayout hubTitle;
    @BindView(R.id.email_password_fields)LinearLayout inputView;
//    @BindView(R.id.loading_bg_image)ImageView loadingBg;
//    @BindView(R.id.login_bg_image)ImageView loginBg;

    private FirebaseAuth mAuth;
    private boolean accountJustCreated;
    private boolean salesAgentSelected;
    private FirebaseUser currentUser;
    private BaseCallback<String> userAccountExistsCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        ButterKnife.bind(this);
        hideStatusBar();
        Realm.init(getApplicationContext());
        RealmUISingleton.getInstance().initRealmInstance();
        UserPreferences.getInstance().setContext(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    if(!showCreateAccountTv.isShown()) passwrodRequirments.setVisibility(View.VISIBLE);
                }else{
                    passwrodRequirments.setVisibility(View.GONE);
                }
            }
        });

        mPasswordField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                animateTitleShrink();
                return false;
            }
        });

        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    animateTitleExpand();
                }
                return false;
            }
        });

        mEmailField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                animateTitleShrink();
                return false;
            }
        });

        mEmailField.setBackPressedListener(this);
        mPasswordField.setBackPressedListener(this);
//        loginBg.setBackground(resizeImage(R.drawable.boot_background_image_resized_1k));
//        loadingBg.setBackground(resizeImage(R.drawable.boot_background_image_resized_1k));
//        DataManager.getInstance().populateDataBaseFakeData();
    }

    public Drawable resizeImage(int imageResource) {// R.drawable.icon
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(),
                getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    @Override
    public void onResume(){
        super.onResume();
        hideStatusBar();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            verificationTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed(){}

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        userAccountExistsCallback = new BaseCallback<String>() {
            @Override
            public void onResponse(String uid) {
                hideLoadingState();
                if(uid != null) {
                    if(currentUser.isEmailVerified()) {
                        launchNextActivity(currentUser);
                    }else{
                        bootLoadingLayout.setVisibility(View.GONE);
                        if(!UserPreferences.getInstance().isVerificationEmailSent()) {
                            sendEmailVerification();
                        }
                    }
                }else if(salesAgentSelected){
                    startActivity(new Intent(getApplicationContext(), UnauthorizedUserActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), CustomerAccountDetailsActivity.class));
                }
            }

            @Override
            public void onFailure(Exception e) {
                hideLoadingState();
            }
        };
        if(currentUser != null) {
            DataManager.getInstance().verifyUserAccountExists(currentUser.getEmail(), userAccountExistsCallback);
        }else{
            bootLoadingLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.sales_agent_tv)
    public void onAccountTypeSalesClicked(){
        salesAgentSelected = true;
    }

    @OnClick(R.id.customer_tv)
    public void onAccointTypeCustomerClicked(){
        salesAgentSelected = false;
    }

    private void animateTitleShrink(){
        verificationTv.setVisibility(View.GONE);
        inputView.animate().translationY(DpUtil.pxFromDp(getApplicationContext(),-100f));
        hubTitle.animate().scaleY(.5f);
        hubTitle.animate().scaleX(.5f);
        hubTitle.animate().translationY(DpUtil.pxFromDp(getApplicationContext(),50f));
    }

    private void animateTitleExpand(){
        inputView.animate().translationY(0f);
        hubTitle.animate().scaleY(1f);
        hubTitle.animate().scaleX(1f);
        hubTitle.animate().translationY(0f);
    }

    private void showLoadingState(){
        createAccountBt.setText("");
        loginBt.setText("");
        loadingIcon.setVisibility(View.VISIBLE);
    }

    private void hideLoadingState(){
        createAccountBt.setText("Create Account");
        loginBt.setText("Sign In");
        loadingIcon.setVisibility(View.GONE);
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            hideLoadingState();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            animateLoginView();
                            accountJustCreated = true;
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            UserPreferences.getInstance().setEmail(currentUser.getEmail());
                            DataManager.getInstance().verifyUserAccountExists(currentUser.getEmail(), userAccountExistsCallback);
                        } else {
                            hideLoadingState();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(BootActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            UserPreferences.getInstance().setEmail(currentUser.getEmail());
                            DataManager.getInstance().verifyUserAccountExists(currentUser.getEmail(), userAccountExistsCallback);
                        } else {
                            hideLoadingState();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(BootActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UserPreferences.getInstance().setVerificationEmailSent(true);
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            verificationTv.setVisibility(View.VISIBLE);
                            animateLoginView();
                        } else {
                            Toast.makeText(BootActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            hideLoadingState();
            valid = false;
        } else if(!StringValidationUtil.isValidEmailAddress(email)){
            mEmailField.setError("Not a valid email.");
            hideLoadingState();
            valid = false;
        } else{
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            hideLoadingState();
            valid = false;
        } else if(StringValidationUtil.isValidPassword(password)){
            mPasswordField.setError("Not a valid password.");
            hideLoadingState();
            valid = false;
        } else{
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void launchNextActivity(FirebaseUser user) {
        if(user.isEmailVerified()) {
            DataManager.getInstance().syncFirebaseToRealmDb();
            currentUser = mAuth.getCurrentUser();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            if(!accountJustCreated) {
                Toast.makeText(this, "Cannot login until email is verified.", Toast.LENGTH_LONG).show();
            }
            verificationTv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.email_create_account_button)
    public void onCreateAccountCLicked(){
        showLoadingState();
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        verificationTv.setVisibility(View.GONE);
    }

    @OnClick(R.id.login_button)
    public void onLoginCLicked(){
        passwrodRequirments.setVisibility(View.GONE);
        showLoadingState();
        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    @OnClick(R.id.show_login_view_tv)
    public void onShowLoginClicked(){
        passwrodRequirments.setVisibility(View.GONE);
        animateLoginView();
    }

    @OnClick(R.id.customer_tv)
    public void onAccountTypeCustomerClicked(){
        customerTv.setBackgroundResource(R.drawable.rounded_rectangle_orange_right);
        salesAgentTv.setBackgroundResource(R.drawable.rounded_rectangle_user_left);
        customerTv.setTextColor(getResources().getColor(R.color.white));
        salesAgentTv.setTextColor(getResources().getColor(R.color.grey_text));
        salesAgentSelected = false;
    }

    @OnClick(R.id.sales_agent_tv)
    public void onSalesAgentAccountTypeClicked(){
        salesAgentSelected = true;
        salesAgentTv.setBackgroundResource(R.drawable.rounded_rectangle_orange_left);
        customerTv.setBackgroundResource(R.drawable.rounded_rectangle_user_right);
        salesAgentTv.setTextColor(getResources().getColor(R.color.white));
        customerTv.setTextColor(getResources().getColor(R.color.grey_text));
    }

    @OnClick(R.id.show_create_account_view_tv)
    public void onShowAccountViewClicked(){
        hideLoadingState();
        animateCreateAccountView();
    }

    private void animateLoginView(){
        animateTitleTextChane("Sign In");
        animateHideAccountType();
        createAccountBt.setVisibility(View.GONE);
        loginBt.setVisibility(View.VISIBLE);
        showLoginViewTv.setVisibility(View.GONE);
        showCreateAccountTv.setVisibility(View.VISIBLE);
        mPasswordField.setHint(getResources().getString(R.string.hint_password));
        if(!UserPreferences.getInstance().getEmail().isEmpty()) {
            mEmailField.setText(UserPreferences.getInstance().getEmail());
        }
    }

    private void animateCreateAccountView(){
        animateTitleTextChane("Create Account");
        animateShowAccountType();
        createAccountBt.setVisibility(View.VISIBLE);
        loginBt.setVisibility(View.GONE);
        showLoginViewTv.setVisibility(View.VISIBLE);
        showCreateAccountTv.setVisibility(View.GONE);
        mPasswordField.setHint(getResources().getString(R.string.new_password));
        mEmailField.setText("");
    }

    private void animateTitleTextChane(final String text){
        bootTitle.animate().scaleX(.0f).setDuration(150);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bootTitle.animate().scaleX(1f).setDuration(150);
                bootTitle.setText(text);
            }
        }, 150);
    }

    private void animateHideAccountType(){
        accountTypeLayout.animate().scaleX(0f).setDuration(250);
        accountTypeTitle.animate().scaleX(0f).setDuration(250);
        accountTypeLayout.animate().scaleY(0f).setDuration(250);
        accountTypeTitle.animate().scaleY(0f).setDuration(250);
        accountTypeLayout.animate().alpha(0f).setDuration(150);
        accountTypeTitle.animate().alpha(0f).setDuration(150);
    }

    private void animateShowAccountType(){
        accountTypeLayout.animate().scaleX(1f).setDuration(150);
        accountTypeTitle.animate().scaleX(1f).setDuration(150);
        accountTypeLayout.animate().scaleY(1f).setDuration(150);
        accountTypeTitle.animate().scaleY(1f).setDuration(150);
        accountTypeLayout.animate().alpha(1f).setDuration(150);
        accountTypeTitle.animate().alpha(1f).setDuration(150);
    }

    @Override
    public void onImeBack(BackAwareEditText editText) {
        hideStatusBar();
        animateTitleExpand();
    }
}
