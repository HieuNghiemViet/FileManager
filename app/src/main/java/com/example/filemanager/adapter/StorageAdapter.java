package com.example.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.R;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.File;
import java.util.ArrayList;

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

//        if (folder.getNameFolder().toLowerCase().endsWith(".jpeg")) {
//            holder.img_folder.setImageResource(R.drawable.ic_image_2);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".jpg")) {
//            holder.img_folder.setImageResource(R.drawable.ic_image_2);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".png")) {
//            holder.img_folder.setImageResource(R.drawable.ic_image_2);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".mp3")) {
//            holder.img_folder.setImageResource(R.drawable.ic_music);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".mp4")) {
//            holder.img_folder.setImageResource(R.drawable.ic_video_2);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".wav")) {
//            holder.img_folder.setImageResource(R.drawable.ic_music);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".pdf")) {
//            holder.img_folder.setImageResource(R.drawable.ic_pdf);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".docx")) {
//            holder.img_folder.setImageResource(R.drawable.ic_docx_file_format_symbol);
//        } else if (folder.getNameFolder().toLowerCase().endsWith(".txt")) {
//            holder.img_folder.setImageResource(R.drawable.ic_txt_file_symbol);
//        }

        holder.img_folder.setImageResource(R.drawable.ic_folder);


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
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

}
