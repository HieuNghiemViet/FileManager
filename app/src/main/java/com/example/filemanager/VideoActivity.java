package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.filemanager.adapter.VideoAdapter;
import com.example.filemanager.callback.OnItemClickListener;

import com.example.filemanager.model.Video;
import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements OnItemClickListener {
    private static final int EDIT_REQUEST_CODE = 1111;
    private ArrayList<Video> arrayList = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerView rcv_video;
    private TextView tv_name_video;
    private TextView tv_path_video;
    private TextView tv_size_video;
    private TextView tv_duration_video;
    private TextView tv_date_video;
    private TextView tv_cancel_dialog_video;
    private Video videotmp;


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
        adapter = new VideoAdapter(arrayList, this, this);
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
            int videoSize = videoCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int videoDuration = videoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int videoDate = videoCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            int videoDisplay = videoCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            do {
                String currentTitle = videoCursor.getString(videoTitle);
                String currentData = videoCursor.getString(videoData);
                long currentSize = videoCursor.getLong(videoSize);
                long currentDuration = videoCursor.getLong(videoDuration);
                long currentDate = videoCursor.getLong(videoDate);
                String currentDisplay = videoCursor.getString(videoDisplay);



                arrayList.add(new Video(currentTitle, currentData, currentSize, currentDuration, currentDate, currentDisplay));
                Log.d("HieuNV", "currentTitle: " +currentTitle);
                Log.d("HieuNV", "currentData: " +currentData);
                Log.d("HieuNV", "currentSize: " +currentSize);
                Log.d("HieuNV", "currentDate: " +currentDate);
                //Log.d("HieuNV", "currentDisplay: " +currentDisplay);
            } while (videoCursor.moveToNext());
        }
    }



    @Override
    public void onClick(int position) {
    }

    @Override
    public void onLongClick(int position) {
        videotmp = arrayList.get(position);

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final String[] feature = {"Thông tin", "Chép vào", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoVideo(Gravity.CENTER, videotmp);
                        break;
                    case 1:
                        cutVideo();
                        break;
                    case 2:
                        renameVideo(Gravity.CENTER, videotmp);
                        break;
                    case 3:
                        deleteDialog(videotmp);
                        break;
                    case 4:
                        shareVideo(videotmp);
                }
            }
        });
        myBuilder.create().show();
    }

    private void shareVideo(Video video) {
        File videoFile = new File(video.getPathVideo());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", videoFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void renameVideo(int center, Video video) {
    }

    private void cutVideo() {
    }

    private void infoVideo(int gravity, Video video) {
        final Dialog dialog = new Dialog(this);
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

    public String getDate(long date){
        date*=1000L;
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
                    arrayList.remove(videotmp);
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
                    startIntentSenderForResult(intentSender, EDIT_REQUEST_CODE,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Video")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteFileUsingDisplayName(VideoActivity.this, video.getDisplayName());
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    deleteFileUsingDisplayName(VideoActivity.this, videotmp.getDisplayName());
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}