package com.ahn.vehiclerentapp.configuration;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SetLanguageSettings {

    Context context;

    public SetLanguageSettings(Context context) {
        this.context = context;
    }

    public void loadLocal () {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = sharedPreferences.getString("lan_settings", "en");
       // setLocal(lang);
    }
}
