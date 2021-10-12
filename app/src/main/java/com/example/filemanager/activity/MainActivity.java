package com.example.filemanager.activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.filemanager.R;
import com.example.filemanager.view.custom.ApplicationStorageView;
import com.example.filemanager.view.custom.DocumentStorageView;
import com.example.filemanager.view.custom.ImageStorageView;
import com.example.filemanager.view.custom.SongStorageView;
import com.example.filemanager.view.custom.VideoStorageView;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int MY_PERMISSION_REQUEST = 1;
    public static final int DELETE_REQUEST_CODE = 123;
    public static final int RENAME_REQUEST_CODE = 300;
    public static final int DELETE_APP_REQUEST_CODE = 400;
    private static final int PERMISSION_REQUEST_CODE = 500;
    private ImageView imageLogo;
    private ImageView imageVideo;
    private ImageView imageSound;
    private ImageView imageDocuments;
    private ImageView imageApp;
    private ImageView imageStorage;
    private ImageStorageView imageStorageView;
    private SongStorageView songStorageView;
    private VideoStorageView videoStorageView;
    private ApplicationStorageView applicationStorageView;
    private DocumentStorageView documentStorageView;
    public static MainActivity sMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMainActivity = this;
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
        songStorageView = findViewById(R.id.activity_song_view);
        videoStorageView = findViewById(R.id.activity_video_view);
        applicationStorageView = findViewById(R.id.activity_application_view);
        documentStorageView = findViewById(R.id.activity_document_view);

        imageLogo.setOnClickListener(this);
        imageVideo.setOnClickListener(this);
        imageSound.setOnClickListener(this);
        imageDocuments.setOnClickListener(this);
        imageApp.setOnClickListener(this);
        imageStorage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (!checkPermission()) {
            askForPermission();
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void askForPermission() {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
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
            songStorageView.openView();
        } else if (v == imageVideo) {
            videoStorageView.openView();
        } else if (v == imageDocuments) {
            documentStorageView.openView();
        } else if (v == imageApp) {
            applicationStorageView.openView();
        } else if (v == imageStorage) {
            Intent intent = new Intent(MainActivity.this, StorageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (imageStorageView.isOpening()) {
            imageStorageView.closeView();
        } else if (songStorageView.isOpening()) {
            songStorageView.closeView();
        } else if (videoStorageView.isOpening()) {
            videoStorageView.closeView();
        } else if (applicationStorageView.isOpening()) {
            applicationStorageView.closeView();
        } else if (documentStorageView.isOpening()) {
            documentStorageView.closeView();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageStorageView.deleteImage();
            try {
                videoStorageView.deleteVideo();
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }

        if ((requestCode == RENAME_REQUEST_CODE && resultCode == Activity.RESULT_OK)) {
            imageStorageView.renameImage();
        }

        if ((requestCode == DELETE_APP_REQUEST_CODE && resultCode == Activity.RESULT_OK )) {
            applicationStorageView.arrayList.remove(applicationStorageView.deletePosition);
            applicationStorageView.adapter.notifyDataSetChanged();
        }

    }

}