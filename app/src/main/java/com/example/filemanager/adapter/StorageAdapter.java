package com.example.filemanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.example.filemanager.ApplicationActivity;
import com.example.filemanager.R;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {

    private ArrayList<Folder> folders;
    private Context context;
    private OnItemClickListener callback;
    private static final int VIEW_TYPE_EMPTY = 2;
    private boolean isSelected = false;

    public StorageAdapter(ArrayList<Folder> folders, Context context, OnItemClickListener callback) {
        this.folders = folders;
        this.context = context;
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nameFolder;
        private TextView tv_numberFile;
        private ImageView img_folder;
        private LinearLayout ln_test;
        private ImageView img_more;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nameFolder = itemView.findViewById(R.id.name_folder);
            tv_numberFile = itemView.findViewById(R.id.number_files);
            img_folder = itemView.findViewById(R.id.img_folder);
            ln_test = itemView.findViewById(R.id.ln_test);
            img_more = itemView.findViewById(R.id.img_more);


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
                    .placeholder(R.drawable.compact_disc)
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
        } else if (folder.getNameFolder().toLowerCase().endsWith(".apk")) {
            holder.img_folder.setImageResource(R.drawable.ic_apk_file);
        } else {
            holder.img_folder.setImageResource(R.drawable.ic_folder);
        }

        int items = 0;
        File[] files = folder.getFile().listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    items += 1;
                }
            }
        }
        if (items > 0) {
            holder.tv_numberFile.setText(items + " Files");
        } else {
            holder.tv_numberFile.setText("empty");
        }



        holder.img_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(position);
            }
        });

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickMore(position);
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
        MediaMetadataRetriever mdr = new MediaMetadataRetriever();
        mdr.setDataSource(path);
        InputStream inputStream = null;
        if (mdr.getEmbeddedPicture() != null) {
            inputStream = new ByteArrayInputStream(mdr.getEmbeddedPicture());
        }
        mdr.release();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
