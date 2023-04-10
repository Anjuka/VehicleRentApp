package com.ahn.vehiclerentapp.ui.driver;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.ahn.vehiclerentapp.models.driver.DriverPostsDataList;
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

public class DriverRegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri mImageUri, licenceImgUri;

    private Button btn_as_client;
    private Spinner sp_nearest_town;
    private CircleImageView circleImageView;
    private LinearLayout ll_licen_pic;
    private ImageView iv_lic;
    private EditText et_full_name;
    private EditText et_nic;
    private EditText et_phone_number;
    private EditText et_address;
    private RadioGroup rg_tour_type;

    private String name = "";
    private String nic = "";
    private String phone_number = "";
    private String address = "";
    private String img = "";
    private String licence_img = "";
    private String tour_type = "";
    private String nearest_town = "";

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private DriverDetails driverDetails;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private int image_type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        driverDetails = new DriverDetails();

        btn_as_client  = findViewById(R.id.btn_as_client);
        sp_nearest_town  = findViewById(R.id.sp_nearest_town);
        circleImageView  = findViewById(R.id.circleImageView);
        ll_licen_pic  = findViewById(R.id.ll_licen_pic);
        iv_lic  = findViewById(R.id.iv_lic);
        et_full_name  = findViewById(R.id.et_full_name);
        et_nic  = findViewById(R.id.et_nic);
        rg_tour_type  = findViewById(R.id.rg_tour_type);
        et_address  = findViewById(R.id.et_address);
        et_phone_number  = findViewById(R.id.et_phone_number);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

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

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(DriverRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
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

        btn_as_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = et_full_name.getText().toString().trim();
                nic = et_nic.getText().toString().trim();
                phone_number = "+94" + et_phone_number.getText().toString().trim();
                address = et_address.getText().toString().trim();

                if (mImageUri == null){
                    String msg_cam = getString(R.string.pic_must_msg);
                    Toast.makeText(DriverRegistrationActivity.this, msg_cam, Toast.LENGTH_SHORT).show();
                }

                if (licenceImgUri == null){
                    String msg_cam = getString(R.string.lic_warn);
                    Toast.makeText(DriverRegistrationActivity.this, msg_cam, Toast.LENGTH_SHORT).show();
                }

                if (name.isEmpty() || nic.isEmpty() || address.isEmpty() || tour_type.isEmpty() ||
                        nearest_town.isEmpty() || phone_number.isEmpty() || mImageUri == null || licenceImgUri == null){
                    String msg = getString(R.string.fill_empty_msg);
                    Toast.makeText(DriverRegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                else {

                    ArrayList<DriverPostsDataList> driverPostsDataLists = new ArrayList<>();
                    ArrayList<String> allocated_time = new ArrayList<>();

                    driverDetails = new DriverDetails(
                            name,
                            nic,
                            phone_number,
                            nearest_town,
                            nearest_town,
                            address,
                            "",
                            mImageUri.toString(),
                            licenceImgUri.toString(),
                            tour_type,
                            driverPostsDataLists,
                            "driver",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "", allocated_time);

                    //registerWithPhone();

                    Intent intent = new Intent(DriverRegistrationActivity.this, VehicleRegActivity.class);
                    intent.putExtra("reg_type", "driver");
                    intent.putExtra("driver_details", driverDetails);
                    startActivity(intent);
                    //finish();
                }

                /*Intent intent = new Intent(DriverRegistrationActivity.this, VehicleRegActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_type = 0;
                captureImages();
            }
        });

        ll_licen_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_type = 1;
                captureImages();
            }
        });

        sp_nearest_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nearest_town = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                nearest_town = cities.get(0);
            }
        });

        rg_tour_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_any:
                        tour_type = "Any";
                        break;
                    case R.id.rb_airport:
                        tour_type = "Airport";
                        break;
                    case R.id.rb_trip:
                        tour_type = "Trip";
                        break;
                    default:
                        tour_type = "Any";
                        break;
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
                Toast.makeText(DriverRegistrationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("SMS", "onVerificationFailed: " + e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verification_code,
                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                hideProgress();
                Intent intent = new Intent(DriverRegistrationActivity.this, DriverOTPVerifyActivity.class);
                intent.putExtra("phone_num", phone_number);
                intent.putExtra("otp", verification_code);
                intent.putExtra("reg_type", "driver");
                intent.putExtra("driver_details", driverDetails);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone_number)
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
                if (image_type == 0) {
                    mImageUri = FileProvider.getUriForFile(this,
                            "com.ahn.vehiclerentapp.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                else {
                    licenceImgUri = FileProvider.getUriForFile(this,
                            "com.ahn.vehiclerentapp.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, licenceImgUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
                mImageUri = data.getData();

           /* else {
                licenceImgUri = data.getData();
                Glide.with(getApplicationContext())
                        .load(licenceImgUri)
                        .placeholder(R.drawable.ic_outline_add_a_photo_24)
                        .into(iv_lic);
            }*/
        }
        if (image_type == 0) {
            Glide.with(getApplicationContext())
                    .load(mImageUri)
                    .placeholder(R.drawable.default_pfp)
                    .into(circleImageView);
        } else {
            Glide.with(getApplicationContext())
                    .load(licenceImgUri)
                    .placeholder(R.drawable.ic_outline_add_a_photo_24)
                    .into(iv_lic);
        }
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