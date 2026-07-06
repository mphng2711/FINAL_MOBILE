package com.example.purepawapp.data.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.purepawapp.util.PrefsKeys;

public class SessionManager {

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PrefsKeys.DATASTORE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedInOnce() {
        return prefs.getBoolean(PrefsKeys.KEY_IS_LOGGED_IN, false);
    }

    public boolean hasSeenOnboardingOnce() {
        return prefs.getBoolean(PrefsKeys.KEY_ONBOARDING_SEEN, false);
    }

    public String getUserIdOnce() {
        return prefs.getString(PrefsKeys.KEY_USER_ID, null);
    }

    public String getRoleOnce() {
        return prefs.getString(PrefsKeys.KEY_ROLE, null);
    }

    public boolean isAdminOnce() {
        return "admin".equals(getRoleOnce());
    }

    public void onLoginSuccess(String userId, String role, boolean rememberMe) {
        prefs.edit()
                .putBoolean(PrefsKeys.KEY_IS_LOGGED_IN, true)
                .putString(PrefsKeys.KEY_USER_ID, userId)
                .putString(PrefsKeys.KEY_ROLE, role)
                .putBoolean(PrefsKeys.KEY_REMEMBER_ME, rememberMe)
                .apply();
    }

    public void markOnboardingSeen() {
        prefs.edit().putBoolean(PrefsKeys.KEY_ONBOARDING_SEEN, true).apply();
    }

    public boolean isBootstrapDoneOnce() {
        return prefs.getBoolean(PrefsKeys.KEY_BOOTSTRAP_DONE, false);
    }

    public void markBootstrapDone() {
        prefs.edit().putBoolean(PrefsKeys.KEY_BOOTSTRAP_DONE, true).apply();
    }

    public void logout() {
        prefs.edit()
                .putBoolean(PrefsKeys.KEY_IS_LOGGED_IN, false)
                .remove(PrefsKeys.KEY_USER_ID)
                .apply();
    }
}
