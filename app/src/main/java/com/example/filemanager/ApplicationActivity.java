package com.example.filemanager;

import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.filemanager.adapter.ApplicationAdapter;
import com.example.filemanager.adapter.SongAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Application;
import com.example.filemanager.model.Image;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity implements OnItemClickListener {
    private int DELETE_REQUEST_CODE = 1000;
    private Drawable iconApp;
    private String nameApp;
    private String packageApp;
    private long sizeApp;
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ArrayList<Application> arrayList = new ArrayList<>();
    private ApplicationInfo applicationInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        try {
            getApp();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        initView();
        setDataAdapter();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_application);
    }

    private void setDataAdapter() {
        adapter = new ApplicationAdapter(arrayList, this, this);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public void getApp() throws PackageManager.NameNotFoundException {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i = 0; i < packages.size(); i++) {
            applicationInfo = packages.get(i);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                iconApp = applicationInfo.loadIcon(pm);
                nameApp = applicationInfo.loadLabel(pm).toString();
                packageApp = applicationInfo.packageName;
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

    @Override
    public void onClick(int position) throws PackageManager.NameNotFoundException {
        Application application = arrayList.get(position);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
        uninstallIntent.setData(Uri.parse("package:" + application.getPackageApp()));
        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(uninstallIntent, DELETE_REQUEST_CODE);
    }


    @Override
    public void onLongClick(int position) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(ApplicationActivity.this, "App uninstall successful", Toast.LENGTH_LONG).show();
                recreate();
            }
        }
    }
}


