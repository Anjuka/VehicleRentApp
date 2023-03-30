package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.adaptes.Sp_adapter;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VehicleRegActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner sp_type;
    private ConstraintLayout cl_pic1;
    private ConstraintLayout cl_pic2;
    private ConstraintLayout cl_pic3;
    private ConstraintLayout cl_pic4;
    private ImageView iv_v_img1;
    private ImageView iv_v_img2;
    private ImageView iv_v_img3;
    private ImageView iv_v_img4;
    private ImageView iv_v_add1;
    private ImageView iv_v_add2;
    private ImageView iv_v_add3;
    private ImageView iv_v_add4;
    private EditText et_nom_vhi;
    private Button btn_as_client;
    private String vehicle_number = "";
    private String vehicle_type= "";

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private ArrayList<String> vehicle_types = new ArrayList<>();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri mImageUri1, mImageUri2, mImageUri3, mImageUri4;
    private int selected_img_position = 0;

    DriverDetails driverDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_reg);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        driverDetails = new DriverDetails();
        driverDetails = (DriverDetails) getIntent().getSerializableExtra("driver_details");

        sp_type = findViewById(R.id.sp_type);
        cl_pic1 = findViewById(R.id.cl_pic1);
        cl_pic2 = findViewById(R.id.cl_pic2);
        cl_pic3 = findViewById(R.id.cl_pic3);
        cl_pic4 = findViewById(R.id.cl_pic4);
        iv_v_img1 = findViewById(R.id.iv_v_img1);
        iv_v_img2 = findViewById(R.id.iv_v_img2);
        iv_v_img3 = findViewById(R.id.iv_v_img3);
        iv_v_img4 = findViewById(R.id.iv_v_img4);
        iv_v_add1 = findViewById(R.id.iv_v_add1);
        iv_v_add2 = findViewById(R.id.iv_v_add2);
        iv_v_add3 = findViewById(R.id.iv_v_add3);
        iv_v_add4 = findViewById(R.id.iv_v_add4);
        btn_as_client = findViewById(R.id.btn_as_client);
        et_nom_vhi = findViewById(R.id.et_nom_vhi);

        cl_pic1.setOnClickListener(this);
        cl_pic2.setOnClickListener(this);
        cl_pic3.setOnClickListener(this);
        cl_pic4.setOnClickListener(this);
        btn_as_client.setOnClickListener(this);

        vehicle_types.add(getString(R.string.car));
        vehicle_types.add(getString(R.string.van));
        vehicle_types.add(getString(R.string.bus));
        vehicle_types.add(getString(R.string.tuk));

        Sp_adapter sp_adapter = new Sp_adapter(VehicleRegActivity.this, vehicle_types);
        sp_type.setAdapter(sp_adapter);

        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicle_type = vehicle_types.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.cl_pic1:
                selected_img_position = 1;
                takePhoto(1);
                break;
            case R.id.cl_pic2:
                selected_img_position = 2;
                takePhoto(2);
                break;
            case R.id.cl_pic3:
                selected_img_position = 3;
                takePhoto(3);
                break;
            case R.id.cl_pic4:
                selected_img_position = 4;
                takePhoto(4);
                break;
            case R.id.btn_as_client:

                vehicle_number = et_nom_vhi.getText().toString().trim();

                if (mImageUri1 == null ||mImageUri2 == null || mImageUri3 == null || mImageUri4 == null){
                    String msg_cam = getString(R.string.plz_vehicle_img);
                    Toast.makeText(VehicleRegActivity.this, msg_cam, Toast.LENGTH_SHORT).show();
                }
                else {
                   if (vehicle_number.isEmpty() || vehicle_types.isEmpty()){
                       String msg_cam = getString(R.string.fill_empty_msg);
                       Toast.makeText(VehicleRegActivity.this, msg_cam, Toast.LENGTH_SHORT).show();
                   }
                   else {
                       driverDetails.setImg_1(mImageUri1.toString());
                       driverDetails.setImg_2(mImageUri1.toString());
                       driverDetails.setImg_3(mImageUri1.toString());
                       driverDetails.setImg_4(mImageUri1.toString());

                       driverDetails.setVehicle_number(vehicle_number);
                       driverDetails.setVehicle_type(vehicle_type);
                       Log.d("TAG", "onClick: driverDetails " + driverDetails);
                       //reg
                       registerWithPhone();
                   }
                }

                break;
        }
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
                Toast.makeText(VehicleRegActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("SMS", "onVerificationFailed: " + e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verification_code,
                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                hideProgress();
                Intent intent = new Intent(VehicleRegActivity.this, DriverOTPVerifyActivity.class);
                intent.putExtra("phone_num", driverDetails.getPhone_number());
                intent.putExtra("otp", verification_code);
                intent.putExtra("reg_type", "driver");
                intent.putExtra("driver_details", driverDetails);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(driverDetails.getPhone_number())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void takePhoto(int image_position) {
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

                switch (image_position){
                    case 1:
                        mImageUri1 = FileProvider.getUriForFile(this,
                                "com.ahn.vehiclerentapp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri1);
                        break;
                    case 2:
                        mImageUri2 = FileProvider.getUriForFile(this,
                                "com.ahn.vehiclerentapp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri2);
                        break;
                    case 3:
                        mImageUri3 = FileProvider.getUriForFile(this,
                                "com.ahn.vehiclerentapp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri3);
                        break;
                    case 4:
                        mImageUri4 = FileProvider.getUriForFile(this,
                                "com.ahn.vehiclerentapp.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri4);
                        break;
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            mImageUri1 = data.getData();
        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            mImageUri1 = data.getData();

        }

        ImageView view = null;

        switch (selected_img_position) {
            case 1:
                view = iv_v_img1;
                iv_v_img1.setVisibility(View.VISIBLE);
                iv_v_add1.setVisibility(View.INVISIBLE);

                if (view != null) {
                    Glide.with(getApplicationContext())
                            .load(mImageUri1)
                            .into(view);
                }
                break;
            case 2:
                view = iv_v_img2;
                iv_v_img2.setVisibility(View.VISIBLE);
                iv_v_add2.setVisibility(View.INVISIBLE);
                if (view != null) {
                    Glide.with(getApplicationContext())
                            .load(mImageUri2)
                            .into(view);
                }
                break;
            case 3:
                view = iv_v_img3;
                iv_v_img3.setVisibility(View.VISIBLE);
                iv_v_add3.setVisibility(View.INVISIBLE);
                if (view != null) {
                    Glide.with(getApplicationContext())
                            .load(mImageUri3)
                            .into(view);
                }
                break;
            case 4:
                view = iv_v_img4;
                iv_v_img4.setVisibility(View.VISIBLE);
                iv_v_add4.setVisibility(View.INVISIBLE);
                if (view != null) {
                    Glide.with(getApplicationContext())
                            .load(mImageUri4)
                            .into(view);
                }
                break;
        }
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