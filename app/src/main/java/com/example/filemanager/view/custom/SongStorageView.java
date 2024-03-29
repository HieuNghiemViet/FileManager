package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.annotation.SuppressLint;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.filemanager.R;
import com.example.filemanager.activity.MainActivity;
import com.example.filemanager.adapter.SongAdapter;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Song;
import com.example.filemanager.util.ScreenUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;


public class SongStorageView extends RelativeLayout implements OnItemClickListener {
    private int RENAME_REQUEST_CODE = 1000;
    private int EDIT_REQUEST_CODE = 111;
    private Context mContext;
    private static Uri extStorageUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private static Uri extDownloadUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private ArrayList<Song> arrayList = new ArrayList<>();
    private Song songTmp;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private TextView tv_info_cancel_song;
    private TextView tv_name_song;
    private TextView tv_name_artist_song;
    private TextView tv_path_song;
    private TextView tv_size_song;
    private TextView tv_date_song;
    private TextView tv_duration_song;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private TextView edt_rename;
    private SwipeRefreshLayout swipe;

    public SongStorageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SongStorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.song_storage_view, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_song_view);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutSong);
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
        adapter = new SongAdapter(arrayList, mContext, this);
        getMusic();
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

    public void getMusic() {
        arrayList.clear();
        ContentResolver contentResolver = mContext.getContentResolver();

        Uri songUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            songUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor songCursor = contentResolver.query(songUri, null, null, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
        if (songCursor != null && songCursor.moveToFirst()) {
            int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songSize = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int songDate = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songDisplay = songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            do {
                String currentName = songCursor.getString(songDisplay);
                String currentArtist = songCursor.getString(songArtist);
                String currentPath = songCursor.getString(songPath);
                long currentSize = songCursor.getLong(songSize);
                long currentDate = songCursor.getLong(songDate);
                long currentDuration = songCursor.getLong(songDuration);
                long currentImage = songCursor.getLong(songImage);
                String currentDisplay = songCursor.getString(songDisplay);

                arrayList.add(new Song(currentImage,
                        currentName,
                        currentArtist,
                        currentPath,
                        currentSize,
                        currentDate,
                        currentDuration,
                        currentDisplay));

            } while (songCursor.moveToNext());
        }
    }

    @Override
    public void onClick(int position) {
        songTmp = arrayList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songTmp.getPath()));
        intent.setDataAndType(Uri.parse(songTmp.getPath()), "audio/*");
        mContext.startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        songTmp = arrayList.get(position);

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(mContext);
        final String[] feature = {"Thông tin", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoSong(Gravity.CENTER, songTmp);
                        break;
                    case 1:
                        renameSong(Gravity.CENTER, songTmp);
                        break;
                    case 2:
                        deleteDialog(songTmp);
                        break;
                    case 3:
                        shareSong(songTmp);
                        break;
                }
            }
        });
        myBuilder.create().show();
    }


    private void renameSong(int gravity, Song song) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rename);

        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
        edt_rename = dialog.findViewById(R.id.edt_rename);
        edt_rename.setText(song.getNameSong());

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String newName = edt_rename.getText().toString();

                try {
                    if (song.getPath().contains("/storage/emulated/0/Download")) {
                        renameFileDownloadUsingDisplayName(mContext, song.getDisplayName());
                    } else {
                        renameFileStorageUsingDisplayName(mContext, song.getDisplayName());
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

    public boolean renameFileDownloadUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
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
            songTmp.setNameSong(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getMusic();
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

    public Long getIdDownloadFromDisplayName(String displayName) {
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

    public boolean renameFileStorageUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        try {
            Long id = getIdStorageFromDisplayName(displayName);
            ContentResolver resolver = context.getContentResolver();
            Uri mUri = ContentUris.withAppendedId(extStorageUri, id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
            contentValues.clear();

            contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, edt_rename.getText().toString());
            contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0);
            contentValues.put(MediaStore.Audio.Media.TITLE, edt_rename.getText().toString());
            resolver.update(mUri, contentValues, null, null);
            songTmp.setNameSong(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getMusic();
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


    private void shareSong(Song song) {
        File songFile = new File(song.getPath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("song/*");
        Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", songFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    public Uri getUriFromDisplayName(Context context, String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(songUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return Uri.parse(songUri.toString() + "/" + fileId);
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
                    arrayList.remove(songTmp);
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

    public void deleteDialog(Song song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Song")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteFileUsingDisplayName(mContext, song.getDisplayName());
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

    public String getDate(long date) {
        date *= 1000L;
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(date));
    }

    public static String dataSizeFormat(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "byte";
        } else if (size < (1 << 20)) {
            float kSize = size >> 10;
            return formater.format(kSize) + "KB";
        } else if (size < (1 << 30)) {
            float mSize = size >> 20;
            return formater.format(mSize) + "MB";
        } else if (size < (1 << 40)) {
            float gSize = size >> 30;
            return formater.format(gSize) + "GB";
        } else {
            return "size : error";
        }
    }

    private void infoSong(int gravity, Song song) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_song);

        tv_info_cancel_song = dialog.findViewById(R.id.tv_cancel_song);
        tv_name_song = dialog.findViewById(R.id.tv_nameSong);
        tv_name_artist_song = dialog.findViewById(R.id.tv_ArtistSong);
        tv_path_song = dialog.findViewById(R.id.tv_pathSong);
        tv_size_song = dialog.findViewById(R.id.tv_sizeSong);
        tv_date_song = dialog.findViewById(R.id.tv_daySong);
        tv_duration_song = dialog.findViewById(R.id.tv_durationSong);

        tv_name_song.setText(song.getNameSong());
        tv_name_artist_song.setText(song.getArtistSong());
        tv_path_song.setText(song.getPath());

        tv_size_song.setText(dataSizeFormat(song.getSize()));

        tv_date_song.setText(getDate(song.getDate()));

        tv_duration_song.setText(convertDuration(song.getDuration()));

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

        tv_info_cancel_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + " : " + minutes + " : " + seconds;
        } else {
            out = minutes + " : " + seconds;
        }
        return out;
    }

}
