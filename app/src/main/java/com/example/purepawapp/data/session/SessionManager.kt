package com.example.purepawapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.purepawapp.util.PrefsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = PrefsKeys.DATASTORE_NAME)

class SessionManager(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey(PrefsKeys.KEY_IS_LOGGED_IN)
        val USER_ID = stringPreferencesKey(PrefsKeys.KEY_USER_ID)
        val ROLE = stringPreferencesKey(PrefsKeys.KEY_ROLE)
        val ONBOARDING_SEEN = booleanPreferencesKey(PrefsKeys.KEY_ONBOARDING_SEEN)
        val REMEMBER_ME = booleanPreferencesKey(PrefsKeys.KEY_REMEMBER_ME)
        val BOOTSTRAP_DONE = booleanPreferencesKey(PrefsKeys.KEY_BOOTSTRAP_DONE)
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[Keys.IS_LOGGED_IN] ?: false }

    val userId: Flow<String?> = context.dataStore.data.map { it[Keys.USER_ID] }

    val role: Flow<String?> = context.dataStore.data.map { it[Keys.ROLE] }

    val onboardingSeen: Flow<Boolean> = context.dataStore.data.map { it[Keys.ONBOARDING_SEEN] ?: false }

    suspend fun isLoggedInOnce(): Boolean = isLoggedIn.first()

    suspend fun hasSeenOnboardingOnce(): Boolean = onboardingSeen.first()

    suspend fun getUserIdOnce(): String? = userId.first()

    suspend fun getRoleOnce(): String? = role.first()

    suspend fun isAdminOnce(): Boolean = getRoleOnce() == "admin"

    suspend fun onLoginSuccess(userId: String, role: String, rememberMe: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = true
            prefs[Keys.USER_ID] = userId
            prefs[Keys.ROLE] = role
            prefs[Keys.REMEMBER_ME] = rememberMe
        }
    }

    suspend fun markOnboardingSeen() {
        context.dataStore.edit { prefs -> prefs[Keys.ONBOARDING_SEEN] = true }
    }

    suspend fun isBootstrapDoneOnce(): Boolean =
        context.dataStore.data.map { it[Keys.BOOTSTRAP_DONE] ?: false }.first()

    suspend fun markBootstrapDone() {
        context.dataStore.edit { prefs -> prefs[Keys.BOOTSTRAP_DONE] = true }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
            prefs.remove(Keys.USER_ID)
        }
    }
}
