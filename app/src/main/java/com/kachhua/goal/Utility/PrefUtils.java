package com.kachhua.goal.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kachhua.goal.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefUtils {

    public static void setCurrentUser(User currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static User getCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        User currentUser = complexPreferences.getObject("current_user_value", User.class);
        return currentUser;
    }

    public static void clearCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


    public static void set_stored_Date(String currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "date_prefs", 0);
        complexPreferences.putObject("Stored_date", currentUser);
        complexPreferences.commit();
    }

    public static String get_stored_Date(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "date_prefs", 0);
        String currentUser = complexPreferences.getObject("Stored_date", String.class);
        return currentUser;
    }

    public static void clear_stored_Date(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "date_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

}
