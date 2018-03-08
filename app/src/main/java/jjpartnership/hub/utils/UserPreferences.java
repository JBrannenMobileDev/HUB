package jjpartnership.hub.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jbrannen on 2/24/18.
 */

public class UserPreferences {
    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final UserPreferences ourInstance = new UserPreferences();
    public Context context;

    public static UserPreferences getInstance() {
        return ourInstance;
    }

    public void setContext(Context context){
        getInstance().context = context;
    }

    private UserPreferences() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public void nukeUserPrefs(){
        getSharedPreferences(context).edit().clear().commit();
    }

    public String getEmail(){
        return getSharedPreferences(context).getString(UserPreferenceConstants.USER_EMAIL, "");
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.USER_EMAIL, email);
        editor.commit();
    }

    public boolean isVerificationEmailSent(){
        return getSharedPreferences(context).getBoolean(UserPreferenceConstants.VERIFICATION_SENT, false);
    }

    public void setVerificationEmailSent(Boolean verificationSent){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(UserPreferenceConstants.VERIFICATION_SENT, verificationSent);
        editor.commit();
    }

    public void setUid(String uid) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UserPreferenceConstants.UID, uid);
        editor.commit();
    }

    public String getUid(){
        return getSharedPreferences(context).getString(UserPreferenceConstants.UID, "");
    }
}
