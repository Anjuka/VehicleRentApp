package com.ahn.vehiclerentapp.ui.driver;

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
import com.ahn.vehiclerentapp.models.UserType;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
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

import java.util.ArrayList;

public class DriverOTPVerifyActivity extends AppCompatActivity {

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
    private String lic_img_url = "";

    private ProgressDialog progressDialog;

    NotificationManager notificationManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    DriverDetails driverDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_otpverify);

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
        driverDetails = (DriverDetails) getIntent().getSerializableExtra("driver_details");
        Log.d("TAG", "onCreate: userDetails " + driverDetails);

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
                                                if (reg_type.equals("driver")) {

                                                    String img_url = driverDetails.getDriver_image();
                                                    if (img_url.equals("") || img_url != null) {
                                                        //upload img
                                                        uploadImageUser(img_url);
                                                    }
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                    } else {
                                        hideProgress();
                                        Toast.makeText(DriverOTPVerifyActivity.this, R.string.otp_wrong, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

    }

    private void uploadImageUser(String img_url) {

        showProgress("Saving...");
        StorageReference image = storageReference.child("images/" + driverDetails.getPhone_number());
        image.putFile(Uri.parse(img_url)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        hideProgress();
                        upload_img_url = uri.toString();
                        progressDialog.cancel();
                        driverDetails.setDriver_image(upload_img_url);
                        uploadLicenceImg();
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

    private void uploadLicenceImg() {

        showProgress("Saving...");
        StorageReference image = storageReference.child("images/" + driverDetails.getPhone_number() + "_lic");
        image.putFile(Uri.parse(driverDetails.getLicence_image())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        hideProgress();
                        lic_img_url = uri.toString();
                        progressDialog.cancel();
                        driverDetails.setDriver_image(lic_img_url);
                        uploadVehicleImg();
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

    private void uploadVehicleImg() {

        ArrayList<Uri> uri_list = new ArrayList<>();
        uri_list.add(Uri.parse(driverDetails.getImg_1()));
        uri_list.add(Uri.parse(driverDetails.getImg_2()));
        uri_list.add(Uri.parse(driverDetails.getImg_3()));
        uri_list.add(Uri.parse(driverDetails.getImg_4()));

        showProgress("Uploading...");
        for (int x =0; x < uri_list.size(); x++){
            int x_val = x;
            StorageReference image = storageReference.child("images/" + driverDetails.getPhone_number() + "_" +driverDetails.getVehicle_number() + "_" + x);
            image.putFile(uri_list.get(x)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            switch (x_val){
                                case 0:
                                    driverDetails.setImg_1(String.valueOf(uri));
                                    break;
                                case 1:
                                    driverDetails.setImg_2(String.valueOf(uri));
                                    break;
                                case 2:
                                    driverDetails.setImg_3(String.valueOf(uri));
                                    break;
                                case 3:
                                    driverDetails.setImg_4(String.valueOf(uri));
                                    break;

                            }

                            if (x_val == 3){
                                updateUserDB();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    private void updateUserDB() {

        showProgress("Saving...");

        DocumentReference documentReference = firebaseFirestore.collection("drivers").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.set(driverDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                hideProgress();
                Intent intent = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                Toast.makeText(DriverOTPVerifyActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
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