package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.models.user.UserPostsDataList;
import com.ahn.vehiclerentapp.ui.otpVerifications.OTPManageActivity;
import com.ahn.vehiclerentapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientRegistrationActivity extends AppCompatActivity {

    private ImageView iv_back;
    private CircleImageView cv_user_img;
    private ConstraintLayout cl_main;
    private ConstraintLayout cl_select_img;
    private Button btn_camera;
    private Button btn_device;
    private Button btn_as_client;
    private ImageView iv_close_select_img;
    private EditText et_name;
    private EditText et_nic;
    private EditText et_host_name;
    private EditText et_br_num;
    private Spinner sp_nearest_town;
    private EditText et_address;
    private EditText et_email;
    // private EditText et_password;
    // private EditText et_password_re;
    private EditText et_mobile_num;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri mImageUri;

    private String full_name = "";
    private String NIC = "";
    private String host_name = "";
    private String br_number = "";
    private String mobile_number = "";
    private String nearest_town = "";
    private String address = "";
    private String email = "";
    private String password = "";
    private String password_re = "";

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();

    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        cl_main = findViewById(R.id.cl_main);
        iv_back = findViewById(R.id.iv_back);
        cv_user_img = findViewById(R.id.circleImageView);
        cl_select_img = findViewById(R.id.cl_select_img);
        btn_camera = findViewById(R.id.btn_camera);
        btn_device = findViewById(R.id.btn_device);
        iv_close_select_img = findViewById(R.id.iv_close_select_img);
        btn_as_client = findViewById(R.id.btn_as_client);
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_name = findViewById(R.id.et_full_name);
        et_nic = findViewById(R.id.et_nic);
        et_host_name = findViewById(R.id.et_host_name);
        et_br_num = findViewById(R.id.et_br);
        sp_nearest_town = findViewById(R.id.sp_nearest_town);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);
        //  et_password = findViewById(R.id.et_password);
        //  et_password_re = findViewById(R.id.et_password_re);

        userDetails = new UserDetails();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        cl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(cl_main);
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

                            for (CityDataList cityDataList1 : cityDataLists) {
                                cities.add(cityDataList1.getCity_name());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            sp_nearest_town.setAdapter(adapter);

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

        sp_nearest_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nearest_town = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cv_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImages();
            }
        });

        iv_close_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_select_img.setVisibility(View.INVISIBLE);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImages();
                cl_select_img.setVisibility(View.INVISIBLE);
            }
        });

        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImages();
                cl_select_img.setVisibility(View.INVISIBLE);
            }
        });

        btn_as_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                full_name = et_name.getText().toString().trim();
                NIC = et_nic.getText().toString().trim();
                host_name = et_host_name.getText().toString().trim();
                mobile_number = et_mobile_num.getText().toString().trim();
                mobile_number = "+94" + mobile_number;
                br_number = et_br_num.getText().toString().trim();
                address = et_address.getText().toString().trim();
                email = et_email.getText().toString().trim();
                //  password = et_password.getText().toString().trim();
                //  password_re = et_password_re.getText().toString().trim();

                //TODO
                //   registerWithPhone();

                if (mImageUri == null || mImageUri.toString().equals("")){
                    String msg_cam = getString(R.string.pic_must_msg);
                    Toast.makeText(ClientRegistrationActivity.this, msg_cam, Toast.LENGTH_SHORT).show();
                }

                if (full_name.isEmpty() || NIC.isEmpty() || host_name.isEmpty() || mobile_number.isEmpty() ||
                        nearest_town.isEmpty() || address.isEmpty() || email.isEmpty()) {

                    String msg = getString(R.string.fill_empty_msg);
                    Toast.makeText(ClientRegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    //client registration process
                    ArrayList<UserPostsDataList> userPostsDataLists = new ArrayList<>();

                    if (mImageUri == null){
                        userDetails = new UserDetails(full_name, NIC, host_name,
                                mobile_number, br_number, nearest_town, address, email, "", userPostsDataLists, "");
                    }
                    else {

                        userDetails = new UserDetails(full_name, NIC, host_name,
                                mobile_number, br_number, nearest_town, address, email, mImageUri.toString(), userPostsDataLists, "");
                    }

                    registerWithPhone();
                }
            }
        });
    }

    private void registerWithPhone() {

        showProgress("Loading...");

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                hideProgress();
                Toast.makeText(ClientRegistrationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("SMS", "onVerificationFailed: " + e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verification_code,
                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                hideProgress();
                Intent intent = new Intent(ClientRegistrationActivity.this, OTPManageActivity.class);
                intent.putExtra("phone_num", mobile_number);
                intent.putExtra("otp", verification_code);
                intent.putExtra("reg_type", "host");
                intent.putExtra("user_details", userDetails);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(mobile_number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void captureImages() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(this,
                        "com.ahn.vehiclerentapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();

        }
        Glide.with(getApplicationContext())
                .load(mImageUri)
                .placeholder(R.drawable.default_pfp)
                .into(cv_user_img);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return imageFile;
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.cancel();
    }

    private void hideKeyboard(ConstraintLayout view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}