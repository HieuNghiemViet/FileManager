package com.example.filemanager.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filemanager.R;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;
import com.example.filemanager.model.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {

    private ArrayList<Folder> folders;
    private Context context;
    private OnItemClickListener callback;

    public StorageAdapter(ArrayList<Folder> folders, Context context, OnItemClickListener callback) {
        this.folders = folders;
        this.context = context;
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nameFolder;
        private TextView tv_numberFile;
        private ImageView img_folder;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nameFolder = itemView.findViewById(R.id.name_folder);
            tv_numberFile = itemView.findViewById(R.id.number_files);
            img_folder = itemView.findViewById(R.id.img_folder);
        }
    }

    @Override
    public StorageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View StorageView = inflater.inflate(R.layout.item_storage, parent, false);
        StorageAdapter.ViewHolder viewHolder = new StorageAdapter.ViewHolder(StorageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StorageAdapter.ViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.tv_nameFolder.setText(folder.getNameFolder());


        if (folder.getNameFolder().toLowerCase().endsWith(".jpeg") ||
                folder.getNameFolder().toLowerCase().endsWith(".jpg") ||
                folder.getNameFolder().toLowerCase().endsWith(".png")) {
            Glide.with(context).load(folder.getPathFolder())
                    .placeholder(R.drawable.ic_image_2)
                    .into(holder.img_folder);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".mp3") || folder.getNameFolder().toLowerCase().endsWith(".wav")) {
            Glide.with(context).load(getAlbumImage(folder.getPathFolder()))
                    .placeholder(R.drawable.ic_music)
                    .into(holder.img_folder);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".mp4")) {
            Glide.with(context).load(folder.getPathFolder())
                    .placeholder(R.drawable.ic_video_2)
                    .into(holder.img_folder);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".pdf")) {
            holder.img_folder.setImageResource(R.drawable.ic_pdf);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".docx")) {
            holder.img_folder.setImageResource(R.drawable.ic_docx_file_format_symbol);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".txt")) {
            holder.img_folder.setImageResource(R.drawable.ic_txt_file_symbol);
        } else if (folder.getNameFolder().toLowerCase().endsWith(".zip")) {
            holder.img_folder.setImageResource(R.drawable.ic_zip);
        } else {
            holder.img_folder.setImageResource(R.drawable.ic_folder);
        }

        Log.d("HieuNV", "folder.getNameFolder(): " + folder.getNameFolder());

        int items = 0;
        File[] files = folder.getFile().listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    items += 1;
                }
            }
        }
        holder.tv_numberFile.setText(items + " Files");

        holder.img_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(position);
            }
        });

        holder.img_folder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    private Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }

}
