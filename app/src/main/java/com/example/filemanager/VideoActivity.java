package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.example.filemanager.adapter.VideoAdapter;
import com.example.filemanager.model.Video;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {
    private ArrayList<Video> arrayList = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerView rcv_video;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        setDataAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataAdapter() {
        rcv_video = (RecyclerView) findViewById(R.id.rcv_video);
        adapter = new VideoAdapter(arrayList, this);
        getVideo();
        rcv_video.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcv_video.setLayoutManager(staggeredGridLayoutManager);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getVideo() {
        ContentResolver contentResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = contentResolver.query(videoUri, null, null, null);
        if (videoCursor != null && videoCursor.moveToFirst()) {
            int videoTitle = videoCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoData = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);

            do {
                String currentTitle = videoCursor.getString(videoTitle);
                String currentData = videoCursor.getString(videoData);
                arrayList.add(new Video(currentTitle, currentData));
            } while (videoCursor.moveToNext());
        }
    }
}