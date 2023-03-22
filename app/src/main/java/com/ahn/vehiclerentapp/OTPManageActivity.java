package com.ahn.vehiclerentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class OTPManageActivity extends AppCompatActivity {

    private ConstraintLayout cl_main;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private EditText et_4;
    private Button btn_verify;

    private int otp_sent;
    private String otp_receive = "";

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpmanage);

        cl_main = findViewById(R.id.cl_main);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);
        et_4 = findViewById(R.id.et_4);
        btn_verify = findViewById(R.id.btn_verify);

        Random random = new Random();
        otp_sent = random.nextInt(9000) + 1000;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "my_channel_id";
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.ic_baseline_mail_lock_24)
                .setContentTitle("DriveMe SL OTP Verification")
                .setContentText(String.valueOf(otp_sent))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(0001, builder.build());

        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_2.requestFocus();
            }
        });

        et_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_3.requestFocus();
            }
        });

        et_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_4.requestFocus();
            }
        });

        et_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_4.clearFocus();
                hideKeyboard(cl_main);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_receive = et_1.getText().toString().trim() + et_2.getText().toString().trim() +
                        et_3.getText().toString().trim() + et_4.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), HostDashoardActivity.class);
                startActivity(intent);

                if (String.valueOf(otp_sent).equals(otp_receive)){

                }
                else {
                    String msg = getString(R.string.otp_incorrect_mdg);
                    Toast.makeText(OTPManageActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard(ConstraintLayout view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}