package pl.edu.pw.ee.overseer.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class SharedPreferencesUtility {
    private static final String NAME = "overseer";
    private static final int MODE = Context.MODE_PRIVATE;

    public static final String KEY_AUTHENTICATED = "key-authenticated";
    public static final String KEY_USERNAME = "key-username";
    public static final String KEY_TOKEN = "key-token";
    public static final String KEY_NAME = "key-name";
    public static final String KEY_EMAIL = "key-email";
    public static final String KEY_MOBILE = "key-mobile";
    public static final String KEY_RANK = "key-rank";
    public static final String KEY_TEAM = "key-team";
    public static final String KEY_DEPARTMENT = "key-department";
    public static final String KEY_COMPANY = "key-company";
    public static final String KEY_SUPERVISOR = "key-supervisor";

    private SharedPreferences mSharedPreferences = null;
    private Editor mEditor = null;

    public SharedPreferencesUtility(Context context) {
        mSharedPreferences = context.getSharedPreferences(NAME, MODE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferencesUtility putString(String key, String value) {
        mEditor.putString(key, value);
        return this;
    }

    public SharedPreferencesUtility putStringSet(String key, Set<String> value) {
        mEditor.putStringSet(key, value);
        return this;
    }

    public SharedPreferencesUtility putInt(String key, int value) {
        mEditor.putInt(key, value);
        return this;
    }

    public SharedPreferencesUtility putLong(String key, long value) {
        mEditor.putLong(key, value);
        return this;
    }

    public SharedPreferencesUtility putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        return this;
    }

    public SharedPreferencesUtility putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        return this;
    }

    public SharedPreferencesUtility remove(String key) {
        mEditor.remove(key);
        return this;
    }

    public void apply() {
        mEditor.apply();
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mSharedPreferences.getStringSet(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public float getFload(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }
}