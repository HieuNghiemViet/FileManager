package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.filemanager.R;
import com.example.filemanager.activity.MainActivity;
import com.example.filemanager.adapter.ApplicationAdapter;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Application;
import com.example.filemanager.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStorageView extends RelativeLayout implements OnItemClickListener {
    private Context mContext;
    private int DELETE_REQUEST_CODE = 1000;
    private Drawable iconApp;
    private String nameApp;
    private String packageApp;
    private long sizeApp;
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ArrayList<Application> arrayList = new ArrayList<>();
    private ApplicationInfo applicationInfo;
    private SwipeRefreshLayout swipe;

    public ApplicationStorageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ApplicationStorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.application_storage_view, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_application_view);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutApplication);
    }

    private void initData() throws PackageManager.NameNotFoundException {
        arrayList.clear();
        getApp();
        setDataAdapter();
    }

    public boolean isOpening() {
        return getVisibility() == VISIBLE;
    }

    public void openView() {
        if (getVisibility() == GONE) {
            MoveAnimation.openViewFromRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        initData();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    public void closeView() {
        MoveAnimation.closeViewToRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void setDataAdapter() {
        adapter = new ApplicationAdapter(arrayList, mContext, this);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                setDataAdapter();
            }
        });

    }

    public void getApp() throws PackageManager.NameNotFoundException {
        final PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i = 0; i < packages.size(); i++) {
            applicationInfo = packages.get(i);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                iconApp = applicationInfo.loadIcon(pm);
                nameApp = applicationInfo.loadLabel(pm).toString();
                packageApp = applicationInfo.packageName;
                sizeApp = getAppSize(mContext, packageApp);
                arrayList.add(new Application(iconApp, nameApp, packageApp, sizeApp));
            }
        }
    }

    public long getAppSize(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return (int) new File(context.getPackageManager().getApplicationInfo(
                packageName, 0).publicSourceDir).length();
    }

    //Fix ve lai adapter khi go ung dung
    @Override
    public void onClick(int position) {
        Application application = arrayList.get(position);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
        uninstallIntent.setData(Uri.parse("package:" + application.getPackageApp()));
        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        MainActivity.sMainActivity.startActivityForResult(uninstallIntent, DELETE_REQUEST_CODE);


        adapter.notifyItemChanged(position);
    }

    @Override
    public void onLongClick(int position) {
    }
}
