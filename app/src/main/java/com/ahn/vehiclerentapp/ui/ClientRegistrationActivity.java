package com.ahn.vehiclerentapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ahn.vehiclerentapp.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientRegistrationActivity extends AppCompatActivity {

    private ImageView iv_back;
    private CircleImageView cv_user_img;
    private ConstraintLayout cl_select_img;
    private Button btn_camera;
    private Button btn_device;
    private ImageView iv_close_select_img;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);

        iv_back = findViewById(R.id.iv_back);
        cv_user_img = findViewById(R.id.circleImageView);
        cl_select_img = findViewById(R.id.cl_select_img);
        btn_camera = findViewById(R.id.btn_camera);
        btn_device = findViewById(R.id.btn_device);
        iv_close_select_img = findViewById(R.id.iv_close_select_img);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cv_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_select_img.setVisibility(View.VISIBLE);
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
}