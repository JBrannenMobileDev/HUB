package jjpartnership.hub.view_layer.activities.boot_activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import jjpartnership.hub.R;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.UserType;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.StringValidationUtil;
import jjpartnership.hub.utils.UserPreferences;
import jjpartnership.hub.view_layer.activities.main_activity.MainActivity;

public class BootActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    @BindView(R.id.field_email)EditText mEmailField;
    @BindView(R.id.field_password)EditText mPasswordField;
    @BindView(R.id.sales_agent_tv) TextView salesAgentTv;
    @BindView(R.id.customer_tv) TextView customerTv;
    @BindView(R.id.account_type_layout)LinearLayout accountTypeLayout;
    @BindView(R.id.email_create_account_button)Button createAccountBt;
    @BindView(R.id.login_button)Button loginBt;
    @BindView(R.id.show_login_view_tv)TextView showLoginViewTv;
    @BindView(R.id.show_create_account_view_tv)TextView showCreateAccountTv;
    @BindView(R.id.account_type_tv)TextView accountTypeTitle;

    private FirebaseAuth mAuth;
    private boolean accountJustCreated;
    private boolean salesAgentSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        ButterKnife.bind(this);
        hideStatusBar();
        Realm.init(getApplicationContext());
        UserPreferences.getInstance().setContext(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            accountJustCreated = true;
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserPreferences.getInstance().setUID(user.getUid());
                            DataManager.getInstance().createNewUser(user.getUid(), user.getEmail(), UserType.getTypeId(salesAgentSelected));
                            updateUI(user);
                            sendEmailVerification();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(BootActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(BootActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
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
                            Toast.makeText(BootActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(BootActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else if(!StringValidationUtil.isValidEmailAddress(email)){
            mEmailField.setError("Not a valid email.");
            valid = false;
        } else{
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else if(StringValidationUtil.isValidPassword(password)){
            mPasswordField.setError("Not a valid password.");
            valid = false;
        } else{
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            launchMainActivity(user);
        }
    }

    private void launchMainActivity(FirebaseUser user) {
        if(user.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else{
            if(!accountJustCreated) {
                Toast.makeText(this, "Cannot login until email is verified.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.email_create_account_button)
    public void onCreateAccountCLicked(){
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    @OnClick(R.id.login_button)
    public void onLoginCLicked(){
        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    @OnClick(R.id.show_login_view_tv)
    public void onShowLoginClicked(){
        animateLoginView();
    }

    @OnClick(R.id.customer_tv)
    public void onAccountTypeCustomerClicked(){
        customerTv.setBackgroundResource(R.drawable.rounded_rectangle_orange_right);
        salesAgentTv.setBackgroundResource(R.drawable.rounded_rectangle_grey_left);
        customerTv.setTextColor(getResources().getColor(R.color.white));
        salesAgentTv.setTextColor(getResources().getColor(R.color.grey_text));
        salesAgentSelected = false;
    }

    @OnClick(R.id.sales_agent_tv)
    public void onSalesAgentAccountTypeClicked(){
        salesAgentSelected = true;
        salesAgentTv.setBackgroundResource(R.drawable.rounded_rectangle_orange_left);
        customerTv.setBackgroundResource(R.drawable.rounded_rectangle_grey_right);
        salesAgentTv.setTextColor(getResources().getColor(R.color.white));
        customerTv.setTextColor(getResources().getColor(R.color.grey_text));
    }

    @OnClick(R.id.show_create_account_view_tv)
    public void onShowAccountViewClicked(){
        animateCreateAccountView();
    }

    private void animateLoginView(){
        accountTypeLayout.animate().translationX(DpUtil.pxFromDp(this, 1000));
        accountTypeTitle.animate().translationX(DpUtil.pxFromDp(this, 1000));
        createAccountBt.setVisibility(View.GONE);
        loginBt.setVisibility(View.VISIBLE);
        showLoginViewTv.setVisibility(View.GONE);
        showCreateAccountTv.setVisibility(View.VISIBLE);
        mPasswordField.setHint(getResources().getString(R.string.hint_password));
    }

    private void animateCreateAccountView(){
        accountTypeLayout.animate().translationX(DpUtil.pxFromDp(this, 0));
        accountTypeTitle.animate().translationX(DpUtil.pxFromDp(this, 0));
        createAccountBt.setVisibility(View.VISIBLE);
        loginBt.setVisibility(View.GONE);
        showLoginViewTv.setVisibility(View.VISIBLE);
        showCreateAccountTv.setVisibility(View.GONE);
        mPasswordField.setHint(getResources().getString(R.string.new_password));
    }
}
