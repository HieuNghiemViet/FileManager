package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.TextView;

import com.example.filemanager.adapter.SongAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Image;
import com.example.filemanager.model.Song;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SongActivity extends AppCompatActivity implements OnItemClickListener {
    private ArrayList<Song> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private TextView tv_info_cancel_song;
    private TextView tv_name_song;
    private TextView tv_path_song;
    private TextView tv_size_song;
    private TextView tv_date_song;
    private TextView tv_duration_song;
    private TextView tv_cancel_song;
    private TextView tv_rename_cancel_song;
    private TextView tv_rename_ok_song;
    private EditText edt_rename_song;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        setDataAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataAdapter() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_song);
        adapter = new SongAdapter(arrayList, this, this);
        getMusic();
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songSize = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int songDate = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_TAKEN);
            int songDuration= songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);


            do {
                String currentName = songCursor.getString(songName);
                String currentArtist = songCursor.getString(songArtist);
                String currentPath = songCursor.getString(songPath);
                long currentSize = songCursor.getLong(songSize);
                long currentDate = songCursor.getLong(songDate);
                int currentDuration = songCursor.getInt(songDuration);
                long currentImage = songCursor.getLong(songImage);

                arrayList.add(new Song(currentImage, currentName, currentArtist, currentPath, currentSize, currentDate, currentDuration));
            } while (songCursor.moveToNext());
        }
    }


    @Override
    public void onClick(int position) {

    }

    @Override
    public void onLongClick(int position) {
        Song songs = arrayList.get(position);

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final String[] feature = {"Thông tin", "Chép vào", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoSong(Gravity.CENTER, songs);
                        break;
                    case 1:
                        cutSong();
                        break;
                    case 2:
                        renameSong(Gravity.CENTER, songs);
                        break;
                    case 3:
                        deleteDialog(songs);
                        deleteSong(songs);
                        break;
                    case 4:
                        shareSong(songs);
                }
            }
        });
        myBuilder.create().show();
    }

    private void renameSong(int gravity, Song song) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_dialog_rename_image);
//
//        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
//        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
//        edt_rename = dialog.findViewById(R.id.edt_rename);
//        edt_rename.setText(song.getNameSong());
//
//        Window window = dialog.getWindow();
//        if (window == null) {
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = gravity;
//        window.setAttributes(windowAttributes);
//        dialog.show();
//
//        tv_rename_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newName = edt_rename.getText().toString();
////                Log.d("HieuNV", "NEW NAME:  " + newName);
////                Log.d("HieuNV", "PATH:  " + image.getPath());
//
////                File dir = new File(image.getPath());
////                Log.d("HieuNV", "dir: " + dir);
////                if (dir.exists()) {
////                    File from = new File(dir, image.getTitle());
////                    File to = new File(dir, "newName.jpg");
////                    if (from.exists()) {
////                        if (from.renameTo(to)) {
////                            Toast.makeText(ImageActivity.this,
////                                    "OK",
////                                    Toast.LENGTH_LONG).show();
////                        }
////                    }
////                }
//
////                String filepath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
////                File from = new File(filepath, image.getTitle());
////                Log.d("HieuNV", "from: " + image.getTitle());
////                File to = new File(filepath, "test.jpg");
////                from.renameTo(to);
//
//                dialog.dismiss();
//            }
//        });
//
//        tv_rename_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
    }

    private void shareSong(Song song) {

        File songFile = new File(song.getPath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("song/*");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", songFile);
        Log.d("HieuNV", "img: " + songFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void cutSong() {
    }

    // DELETE IMAGE FIX
    private void deleteSong(Song song) {
        String path = song.getPath();
        File file = new File(path);
        // Log.d("HieuNV", " " + file.getAbsolutePath());
        try {
            // Log.d("HieuNV", "path: " + path);
            if (file.exists()) {
                // Log.d("HieuNV", "exits: true");
                file.delete();
                //Log.d("HieuNV", "deleteImage: true");
            }
        } catch (Exception e) {
            Log.d("HieuNV", " " + e);
        }
    }

    public void deleteDialog(Song song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Song")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSong(song);
                        arrayList.remove(song);
                        adapter.notifyDataSetChanged();
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

    private void infoSong(int gravity, Song song) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_song);

        tv_info_cancel_song = dialog.findViewById(R.id.tv_cancel_song);
        tv_name_song = dialog.findViewById(R.id.tv_nameSong);
        tv_path_song = dialog.findViewById(R.id.tv_pathSong);
        tv_size_song = dialog.findViewById(R.id.tv_sizeSong);
        tv_date_song = dialog.findViewById(R.id.tv_daySong);
        tv_duration_song = dialog.findViewById(R.id.tv_durationSong);

        tv_name_song.setText(song.getNameSong());
        tv_path_song.setText(song.getPath());
        tv_size_song.setText(Formatter.formatShortFileSize(dialog.getContext(), song.getSize()));

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        tv_date_song.setText(sdf.format(song.getDate()));

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
}