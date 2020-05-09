package com.example.silaper.configfile;

import android.content.Context;
import android.content.SharedPreferences;

public class authdata {
    private static authdata mInstance;
    public static Context mCtx;

    public static final String SHARED_PREF_NAME = "sharedsilaper";
    private static final String idcosturmer = "n";

    private authdata(Context context){
        mCtx = context;
    }
    public static synchronized authdata getInstance(Context context){
        if (mInstance == null){
            mInstance = new authdata(context);
        }
        return mInstance;
    }

    public boolean setIdcosturmer (String xid){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(idcosturmer, xid);
        editor.apply();

        return true;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }



    public String getIdcosturmer() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(idcosturmer, null);
    }

}
