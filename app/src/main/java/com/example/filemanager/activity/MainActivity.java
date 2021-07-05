package com.example.filemanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.filemanager.R;
import com.example.filemanager.view.custom.ImageStorageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int MY_PERMISSION_REQUEST = 1;
    private ImageView imageLogo;
    private ImageView imageVideo;
    private ImageView imageSound;
    private ImageView imageDocuments;
    private ImageView imageApp;
    private ImageView imageStorage;
    private ImageStorageView imageStorageView;
    private int DELETE_REQUEST_CODE = 123;
    private int RENAME_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        imageLogo = (ImageView) findViewById(R.id.img_logo_image);
        imageVideo = (ImageView) findViewById(R.id.img_logo_video);
        imageSound = (ImageView) findViewById(R.id.img_logo_sound);
        imageDocuments = (ImageView) findViewById(R.id.img_logo_documents);
        imageApp = (ImageView) findViewById(R.id.img_logo_app);
        imageStorage = (ImageView) findViewById(R.id.img_logo_storage);
        imageStorageView = findViewById(R.id.activity_image_view);

        imageLogo.setOnClickListener(this);
        imageVideo.setOnClickListener(this);
        imageSound.setOnClickListener(this);
        imageDocuments.setOnClickListener(this);
        imageApp.setOnClickListener(this);
        imageStorage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        askForPermission();
    }

    public void askForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "No Permission granted", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageLogo) {
            imageStorageView.openView();

        } else if (v == imageSound) {
            Intent intent = new Intent(MainActivity.this, SongActivity.class);
            startActivity(intent);
        } else if (v == imageVideo) {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        } else if (v == imageDocuments) {
            Intent intent = new Intent(MainActivity.this, DocumentActivity.class);
            startActivity(intent);
        } else if (v == imageApp) {
            Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
            startActivity(intent);
        } else if (v == imageStorage) {
            Intent intent = new Intent(MainActivity.this, StorageActivity.class);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageStorageView.deleteImage();
            }
        }
        if ((requestCode == RENAME_REQUEST_CODE)) {
            if (resultCode == Activity.RESULT_OK) {
                imageStorageView.renameImage();
            }
        }
    }
}