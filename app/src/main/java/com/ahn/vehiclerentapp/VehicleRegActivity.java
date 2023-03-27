package com.ahn.vehiclerentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ahn.vehiclerentapp.adaptes.Sp_adapter;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    private ArrayList<String> vehicle_type = new ArrayList<>();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri mImageUri;
    private int selected_img_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_reg);

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

        cl_pic1.setOnClickListener(this);
        cl_pic2.setOnClickListener(this);
        cl_pic3.setOnClickListener(this);
        cl_pic4.setOnClickListener(this);

        vehicle_type.add(getString(R.string.car));
        vehicle_type.add(getString(R.string.van));
        vehicle_type.add(getString(R.string.bus));
        vehicle_type.add(getString(R.string.tuk));

        Sp_adapter sp_adapter = new Sp_adapter(VehicleRegActivity.this, vehicle_type);
        sp_type.setAdapter(sp_adapter);
        ;
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
        }
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
                mImageUri = FileProvider.getUriForFile(this,
                        "com.ahn.vehiclerentapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();

        }

        ImageView view = null;

        switch (selected_img_position) {
            case 1:
                view = iv_v_img1;
                iv_v_img1.setVisibility(View.VISIBLE);
                iv_v_add1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                view = iv_v_img2;
                iv_v_img2.setVisibility(View.VISIBLE);
                iv_v_add2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                view = iv_v_img3;
                iv_v_img3.setVisibility(View.VISIBLE);
                iv_v_add3.setVisibility(View.INVISIBLE);
                break;
            case 4:
                view = iv_v_img4;
                iv_v_img4.setVisibility(View.VISIBLE);
                iv_v_add4.setVisibility(View.INVISIBLE);
                break;
        }
        if (view != null) {
            Glide.with(getApplicationContext())
                    .load(mImageUri)
                    .into(view);
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
}