package com.example.filemanager.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filemanager.R;
import com.example.filemanager.StorageActivity;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {

    private ArrayList<Folder> folders;
    private Context context;
    private OnItemClickListener callback;

    public StorageAdapter(ArrayList<Folder> folders,Context context ,OnItemClickListener callback) {
        this.folders = folders;
        this.context = context;
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nameFolder;
        private TextView tv_dateFolder;
        private TextView tv_numberFile;
        private ImageView img_folder;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nameFolder = itemView.findViewById(R.id.name_folder);
            tv_dateFolder = itemView.findViewById(R.id.date_folder);
            tv_numberFile = itemView.findViewById(R.id.number_files);
            img_folder = itemView.findViewById(R.id.img_folder);

        }
    }

    @Override
    public StorageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.item_storage, parent, false);
        StorageAdapter.ViewHolder viewHolder = new StorageAdapter.ViewHolder(songView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StorageAdapter.ViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.tv_nameFolder.setText(folder.getNameFolder());
        holder.tv_dateFolder.setText(folder.getDateFolder());
        holder.tv_numberFile.setText(folder.getNumberFile());

        holder.img_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    callback.onClick(position);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
