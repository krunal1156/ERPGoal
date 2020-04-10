package com.kachhua.goal.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ComplexPreferences {

    private static ComplexPreferences complexPreferences;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();
    //Type typeOfObject = new TypeToken<Object>() {}.getType();

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "complex_preferences";
        }
        try{

            preferences = context.getSharedPreferences(namePreferences, mode);
            editor = preferences.edit();

        }catch (Exception e){

        }

    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences, int mode)
    {

//		if (complexPreferences == null) {
        complexPreferences = new ComplexPreferences(context, namePreferences, mode);
//		}

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if(object == null)
        {
            throw new IllegalArgumentException("object is null");
        }
        if(key.equals("") || key == null)
        {
            throw new IllegalArgumentException("key is empty or null");
        }

        try{
            editor.putString(key, GSON.toJson(object));
        }catch (Exception e){}

    }







    public void commit() {
        editor.commit();
    }
    public void clearObject() {
        editor.clear();
    }
    public <T> T getObject(String key, Class<T> a) {

        String gson ="";
        try{
            gson = preferences.getString(key, null);
        }catch (Exception e){}

        if (gson == null) {
            return null;
        }
        else {
            try{
                return GSON.fromJson(gson, a);

            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }
}
