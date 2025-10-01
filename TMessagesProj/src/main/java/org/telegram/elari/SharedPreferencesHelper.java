package org.telegram.elari;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {
    private static final String SP_ELARI = "ELARI";
    public static final String SP_PREFIX_ACCES_ELEMENT = "SP_PREFIX_ACCES_ELEMENT_";
    public static final String SP_PREFIX_REQUEST_WAS_SEND = "SP_PREFIX_REQUEST_WAS_SEND_";
    public static final String PHONE_NUMBER_KEY = "phone";
    public static final String PHONE_NUMBER = "number";

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesHelper(Context context) {
        if(context!=null) {
            this.sharedPreferences = context.getSharedPreferences(SP_ELARI, Context.MODE_PRIVATE);
        }
        this.context = context;
    }


    public void setCustomValue(@NonNull String key, @NonNull String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }
    public void setCustomValue(@NonNull String key, @NonNull Long data) {
        sharedPreferences.edit().putLong(key, data).apply();
    }
    public void setCustomValue(@NonNull String key, @NonNull Integer data) {
        sharedPreferences.edit().putInt(key, data).apply();
    }
    public void setCustomValue(@NonNull String key, @NonNull Boolean data) {
        sharedPreferences.edit().putBoolean(key, data).apply();
    }


    public String getCustomValue(@NonNull String key) {
        return sharedPreferences.getString(key, "");
    }
    public Long getLongCustomValue(@NonNull String key) {
        return sharedPreferences.getLong(key, 0);
    }
    public Integer getIntCustomValue(@NonNull String key) {
        return sharedPreferences.getInt(key, 0);
    }
    public Boolean getBooleanCustomValue(@NonNull String key) {
        return sharedPreferences.getBoolean(key, false);
    }
    public Boolean getBooleanCustomValue(@NonNull String key, boolean defaultval) {
        return sharedPreferences.getBoolean(key, defaultval);
    }


    public void setSharedPreferenceStringList(String pKey, List<String> pData) {
        SharedPreferences.Editor editor = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).edit();
        editor.putInt(pKey + "size", pData.size());
        editor.commit();

        for (int i = 0; i < pData.size(); i++) {
            SharedPreferences.Editor editor1 = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).edit();
            editor1.putString(pKey + i, (pData.get(i)));
            editor1.commit();
        }
    }

    public List<String> getSharedPreferenceStringList(String pKey) {
        int size = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).getInt(pKey + "size", 0);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).getString(pKey + i, ""));
        }
        return list;
    }



    public void setSharedPreferenceLongList(String pKey, List<Long> pData) {
        SharedPreferences.Editor editor = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).edit();
        editor.putInt(pKey + "size", pData.size());
        editor.commit();

        for (int i = 0; i < pData.size(); i++) {
            SharedPreferences.Editor editor1 = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).edit();
            editor1.putLong(pKey + i, (pData.get(i)));
            editor1.commit();
        }
    }

    public List<Long> getSharedPreferenceLongList(String pKey) {
        int size = context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).getInt(pKey + "size", 0);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(context.getSharedPreferences("session_"+ pKey, context.MODE_PRIVATE).getLong(pKey + i, 0));
        }
        return list;
    }
}
