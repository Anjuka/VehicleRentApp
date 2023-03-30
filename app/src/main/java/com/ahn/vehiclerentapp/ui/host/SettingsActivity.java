package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private RadioGroup rbg_lang;
    private RadioButton rb_english;
    private RadioButton rb_sinhala;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();

    private Spinner et_nearest_town;
    private ImageView iv_back;
    private BottomNavigationView bottomNavigationView;

    private String language ="";

    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);

        userDetails = new UserDetails();
        userDetails = (UserDetails) getIntent().getSerializableExtra("user_data");

        rbg_lang = findViewById(R.id.rbg_lang);
        rb_english = findViewById(R.id.rb_english);
        rb_sinhala = findViewById(R.id.rb_sinhala);
        et_nearest_town = findViewById(R.id.et_nearest_town);
        iv_back = findViewById(R.id.iv_back);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        iv_back.setVisibility(View.INVISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentDashboard = new Intent(getApplicationContext(), HostDashoardActivity.class);
                        startActivity(intentDashboard);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                        intentProfile.putExtra("user_data", userDetails);
                        startActivity(intentProfile);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about_us:
                        Intent intentAboutUS = new Intent(getApplicationContext(), AboutUsActivity.class);
                        intentAboutUS.putExtra("user_data", userDetails);
                        startActivity(intentAboutUS);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        /*Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                        intentSettings.putExtra("user_data", userDetails);
                        startActivity(intentSettings);
                        finish();
                        overridePendingTransition(0, 0);*/
                        return true;
                }
                return false;
            }
        });

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String language = sharedPref.getString("LOCALE", "en");
        //language = Locale.getDefault().getDisplayLanguage();
        Log.d("TAG", "onCreate: ");
        if (language.equalsIgnoreCase("en")){
            rb_english.setChecked(true);
            //rb_sinhala.setChecked(false);
        }
        else {
            rb_sinhala.setChecked(true);
            //rb_english.setChecked(false);
        }

        rb_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(0);
            }
        });

        rb_sinhala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(1);
            }
        });

        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseFirestore.collection("cities")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<CityData> cityDataList = querySnapshot.toObjects(CityData.class);
                        cityDataLists.clear();
                        if (cityDataList.get(0).getArrayList() != null) {
                            cityDataLists.addAll(cityDataList.get(0).getArrayList());
                            Log.d("TAG", "onSuccess: ");

                            for (CityDataList cityDataList1 : cityDataLists){
                                cities.add(cityDataList1.getCity_name());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            et_nearest_town.setAdapter(adapter);

                            for (int x=0; x < cityDataLists.size(); x++){
                                if (cityDataLists.get(x).getCity_name().equalsIgnoreCase(userDetails.getActive_town())){
                                    et_nearest_town.setSelection(x);
                                }
                            }

                            progressDialog.cancel();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
                        progressDialog.cancel();
                    }
                });

        Log.d("TAG", "onCreate: cityDataLists " + cityDataLists);
    }

    private void showAlert(int type) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingsActivity.this);
        String msg = getString(R.string.langu_warning);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.proceed,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                       if (type == 0){
                           setLocal("en");
                           recreate();
                           SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPref.edit();
                           editor.putString("LOCALE", "en");
                           editor.commit();
                       }
                       else {
                           setLocal("si");
                           recreate();
                           SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPref.edit();
                           editor.putString("LOCALE", "si");
                           editor.commit();
                       }
                    }
                });

        builder1.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
}