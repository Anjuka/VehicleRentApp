package com.ahn.vehiclerentapp.otpVerifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.UserDetails;
import com.ahn.vehiclerentapp.models.UserType;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OTPManageActivity extends AppCompatActivity {

    private ConstraintLayout cl_main;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;
    private EditText et_4;
    private EditText et_5;
    private EditText et_6;
    private Button btn_verify;

    private String code;
    private String otp_receive = "";
    private String phone_num_receive = "";
    private String reg_type = "";
    private String upload_img_url = "";

    private ProgressDialog progressDialog;

    NotificationManager notificationManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpmanage);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        cl_main = findViewById(R.id.cl_main);
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);
        et_4 = findViewById(R.id.et_4);
        et_5 = findViewById(R.id.et_5);
        et_6 = findViewById(R.id.et_6);
        btn_verify = findViewById(R.id.btn_verify);

        otp_receive = getIntent().getExtras().getString("otp");
        phone_num_receive = getIntent().getExtras().getString("phone_num");
        reg_type = getIntent().getExtras().getString("reg_type");
        userDetails = (UserDetails) getIntent().getSerializableExtra("user_details");
        Log.d("TAG", "onCreate: userDetails " + userDetails);

        /*Random random = new Random();
        otp_sent = random.nextInt(9000) + 1000;*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        notificationManager.notify(0001, builder.build());*/

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
                et_5.requestFocus();
            }
        });

        et_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_6.requestFocus();
            }
        });

        et_6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_6.clearFocus();
                hideKeyboard(cl_main);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = et_1.getText().toString().trim() + et_2.getText().toString().trim() +
                        et_3.getText().toString().trim() + et_4.getText().toString().trim() +
                        et_5.getText().toString().trim() + et_6.getText().toString().trim();

                showProgress("Verifying...");

                if (code.length() == 6) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp_receive, code);
                    FirebaseAuth.getInstance().
                            signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        hideProgress();

                                        UserType userType = new UserType(reg_type);

                                        DocumentReference documentReference = firebaseFirestore.collection("users_type").document(firebaseAuth.getCurrentUser().getUid());
                                        documentReference.set(userType).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                //save user data
                                                hideProgress();
                                                if (reg_type.equals("host")) {

                                                    String img_url = userDetails.getImage();
                                                    if (img_url.equals("") || img_url != null) {
                                                        //upload img
                                                        uploadImage(img_url);
                                                    }
                                                    else {
                                                        updateUserData(upload_img_url);
                                                    }
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                    } else {
                                        hideProgress();
                                        Toast.makeText(OTPManageActivity.this, R.string.otp_wrong, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

    private void uploadImage(String img_url) {

        showProgress("Saving...");
        StorageReference image = storageReference.child("images/" + userDetails.getPhone_number());
        image.putFile(Uri.parse(img_url)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        hideProgress();
                        upload_img_url = uri.toString();
                        progressDialog.cancel();
                        updateUserData(upload_img_url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        hideProgress();
                        progressDialog.cancel();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    private void updateUserData(String upload_img_url) {
        showProgress("Saving...");
        userDetails.setImage(upload_img_url);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                hideProgress();
                Intent intent = new Intent(getApplicationContext(), HostDashoardActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                Toast.makeText(OTPManageActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard(ConstraintLayout view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.cancel();
    }
}