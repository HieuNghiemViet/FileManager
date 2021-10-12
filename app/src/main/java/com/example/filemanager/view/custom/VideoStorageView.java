package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.filemanager.R;
import com.example.filemanager.activity.MainActivity;
import com.example.filemanager.adapter.VideoAdapter;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Video;
import com.example.filemanager.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class VideoStorageView extends RelativeLayout implements OnItemClickListener {
    private static Uri extStorageUri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private static Uri extDownloadUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private int EDIT_REQUEST_CODE = 1111;
    private int RENAME_REQUEST_CODE = 2222;
    private ArrayList<Video> arrayList = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerView rcv_video;
    private TextView tv_name_video;
    private TextView tv_path_video;
    private TextView tv_size_video;
    private TextView tv_duration_video;
    private TextView tv_date_video;
    private TextView tv_cancel_dialog_video;
    private Video videoTmp;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private EditText edt_rename;
    private SwipeRefreshLayout swipe;
    private Context mContext;

    public VideoStorageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public VideoStorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }



    public void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.video_storage_view, this);
        rcv_video = (RecyclerView) rootView.findViewById(R.id.rcv_video_view);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutVideo);

    }

    private void initData() {
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
                    initData();
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
        adapter = new VideoAdapter(arrayList, mContext, this);
        getVideo();
        rcv_video.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcv_video.setLayoutManager(staggeredGridLayoutManager);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setDataAdapter();
                swipe.setRefreshing(false);
            }
        });
    }


    public void getVideo() {
        arrayList.clear();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = contentResolver.query(videoUri, null, null, null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
        if (videoCursor != null && videoCursor.moveToFirst()) {
            int videoTitle = videoCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoData = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int videoSize = videoCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int videoDuration = videoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int videoDate = videoCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            int videoDisplay = videoCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            do {
                String currentTitle = videoCursor.getString(videoDisplay);
                String currentData = videoCursor.getString(videoData);
                long currentSize = videoCursor.getLong(videoSize);
                long currentDuration = videoCursor.getLong(videoDuration);
                long currentDate = videoCursor.getLong(videoDate);
                String currentDisplay = videoCursor.getString(videoDisplay);

                arrayList.add(new Video(currentTitle, currentData, currentSize, currentDuration, currentDate, currentDisplay));
            } while (videoCursor.moveToNext());
        }
    }


    @Override
    public void onClick(int position) {
        videoTmp = arrayList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoTmp.getPathVideo()));
        intent.setDataAndType(Uri.parse(videoTmp.getPathVideo()), "video/*");
        mContext.startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        videoTmp = arrayList.get(position);

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(mContext);
        final String[] feature = {"Thông tin", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoVideo(Gravity.CENTER, videoTmp);
                        break;
                    case 1:
                        renameVideo(Gravity.CENTER, videoTmp);
                        break;
                    case 2:
                        deleteDialog(videoTmp);
                        break;
                    case 3:
                        shareVideo(videoTmp);
                        break;
                }
            }
        });
        myBuilder.create().show();
    }


    private void shareVideo(Video video) {
        File videoFile = new File(video.getPathVideo());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", videoFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void renameVideo(int gravity, Video video) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rename);

        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
        edt_rename = dialog.findViewById(R.id.edt_rename);
        edt_rename.setText(video.getNameVideo());

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_rename_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (video.getPathVideo().contains("/storage/emulated/0/Download")) {
                        renameFileDownloadUsingDisplayName(mContext, video.getDisplayName());
                    } else {
                        renameFileStorageUsingDisplayName(mContext, video.getDisplayName());
                    }
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        tv_rename_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public boolean renameFileStorageUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        try {
            Long id = getIdStorageFromDisplayName(displayName);
            ContentResolver resolver = context.getContentResolver();
            Uri mUri = ContentUris.withAppendedId(extStorageUri, id);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
            contentValues.clear();

            contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, edt_rename.getText().toString());
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
            contentValues.put(MediaStore.Video.Media.TITLE, edt_rename.getText().toString());
            resolver.update(mUri, contentValues, null, null);
            videoTmp.setNameVideo(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getVideo();
            return true;

        } catch (SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException) securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender = recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                MainActivity.sMainActivity.startIntentSenderForResult(intentSender, RENAME_REQUEST_CODE,
                        null, 0, 0, 0, null);
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
        return false;
    }

    public Long getIdStorageFromDisplayName(String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Cursor cursor = mContext.getContentResolver().query(extStorageUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return fileId;
        }
        return null;
    }

    private boolean renameFileDownloadUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        try {
            Long id = getIdDownloadFromDisplayName(displayName);
            ContentResolver resolver = context.getContentResolver();
            Uri mUri = ContentUris.withAppendedId(extDownloadUri, id);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
            contentValues.clear();

            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, edt_rename.getText().toString());
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0);
            contentValues.put(MediaStore.Downloads.TITLE, edt_rename.getText().toString());
            resolver.update(mUri, contentValues, null, null);
            videoTmp.setNameVideo(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getVideo();
            return true;
        } catch (SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException) securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender = recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                MainActivity.sMainActivity.startIntentSenderForResult(intentSender, RENAME_REQUEST_CODE,
                        null, 0, 0, 0, null);
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
        return false;
    }

    private Long getIdDownloadFromDisplayName(String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Cursor cursor = mContext.getContentResolver().query(extDownloadUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);
        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return fileId;
        }
        return null;
    }

    private void infoVideo(int gravity, Video video) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_video);

        tv_name_video = dialog.findViewById(R.id.tv_nameVideo);
        tv_path_video = dialog.findViewById(R.id.tv_pathVideo);
        tv_size_video = dialog.findViewById(R.id.tv_sizeVideo);
        tv_date_video = dialog.findViewById(R.id.tv_dayVideo);
        tv_duration_video = dialog.findViewById(R.id.tv_durationVideo);
        tv_cancel_dialog_video = dialog.findViewById(R.id.tv_cancel_video);

        tv_name_video.setText(video.getNameVideo());
        tv_path_video.setText(video.getPathVideo());
        tv_size_video.setText(Formatter.formatShortFileSize(dialog.getContext(), video.getSizeVideo()));

        tv_date_video.setText(getDate(video.getDateVideo()));

        tv_duration_video.setText(convertDuration(video.getDurationVideo()));

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_cancel_dialog_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public String getDate(long date) {
        date *= 1000L;
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(date));
    }

    private String convertDuration(long durationVideo) {
        String out = null;
        long hours = 0;
        try {
            hours = (durationVideo / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (durationVideo - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (durationVideo - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }
        return out;
    }

    public Uri getUriFromDisplayName(Context context, String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Uri imgUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(imgUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return Uri.parse(imgUri.toString() + "/" + fileId);
        } else {
            return null;
        }
    }

    public boolean deleteFileUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        Uri uri = getUriFromDisplayName(context, displayName);
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            String[] selectionArgsPdf = new String[]{displayName};

            try {
                if (resolver.delete(uri, MediaStore.Files.FileColumns.DISPLAY_NAME + "=?", selectionArgsPdf) > 0) {
                    arrayList.remove(videoTmp);
                    adapter.notifyDataSetChanged();
                }
                return true;
            } catch (SecurityException securityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    RecoverableSecurityException recoverableSecurityException;
                    if (securityException instanceof RecoverableSecurityException) {
                        recoverableSecurityException =
                                (RecoverableSecurityException) securityException;
                    } else {
                        throw new RuntimeException(
                                securityException.getMessage(), securityException);
                    }
                    IntentSender intentSender = recoverableSecurityException.getUserAction()
                            .getActionIntent().getIntentSender();
                    MainActivity.sMainActivity.startIntentSenderForResult(intentSender, EDIT_REQUEST_CODE,
                            null, 0, 0, 0, null);
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
            }
        }
        return false;
    }

    public void deleteDialog(Video video) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Video")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteFileUsingDisplayName(mContext, video.getDisplayName());
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void deleteVideo() throws IntentSender.SendIntentException {
        deleteFileUsingDisplayName(mContext, videoTmp.getDisplayName());
    }
}
