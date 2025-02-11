package ph.appdev.grocerylistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static ph.appdev.grocerylistapp.PreferencesUtility.*;

public class SavedSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */

    public static void setLoggedIn(Context context, boolean loggedIn, String email) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.putString(LOGGED_USER, email);
        editor.apply();
    }



    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);

    }
    public static String getLoggedUser(Context context) {
        return getPreferences(context).getString(LOGGED_USER, "");

    }

}
