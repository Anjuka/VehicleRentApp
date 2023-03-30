package com.ahn.vehiclerentapp.ui.languageSelection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;

import java.util.Locale;

public class ChooseLanguageActivity extends AppCompatActivity {

    private TextView cl_english;
    private TextView cl_sinhala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        cl_english = findViewById(R.id.cl_english);
        cl_sinhala = findViewById(R.id.cl_sinhala);

        cl_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocal("en");
                recreate();

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("LOCALE", "en");
                editor.commit();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        cl_sinhala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocal("si");
                recreate();

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("LOCALE", "si");
                editor.commit();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void setLocal(String lan) {

        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale= locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("lan_settings",lan);
        editor.apply();
    }

    public void loadLocal (){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = sharedPreferences.getString("lan_settings", "en");
        setLocal(lang);
    }
}