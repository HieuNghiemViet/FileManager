package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.example.filemanager.adapter.ApplicationAdapter;
import com.example.filemanager.adapter.SongAdapter;
import com.example.filemanager.model.Application;
import com.example.filemanager.model.Image;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    private Drawable iconApp;
    private String nameApp;
    private String packageApp;
    private long sizeApp;
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ArrayList<Application> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        try {
            getApp();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.rcv_application);
        adapter = new ApplicationAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public void getApp() throws PackageManager.NameNotFoundException {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (int i = 0; i < packages.size(); i++) {
            ApplicationInfo packageInfo = packages.get(i);
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                iconApp = packageInfo.loadIcon(pm);
                nameApp = packageInfo.loadLabel(pm).toString();
                packageApp = packageInfo.packageName;
                sizeApp = getAppSize(this, packageApp);
                arrayList.add(new Application(iconApp, nameApp, packageApp, sizeApp));
            }
        }
    }

    public long getAppSize(Context context, String packageName)
            throws PackageManager.NameNotFoundException {
        return (int) new File(context.getPackageManager().getApplicationInfo(
                packageName, 0).publicSourceDir).length();
    }

}


