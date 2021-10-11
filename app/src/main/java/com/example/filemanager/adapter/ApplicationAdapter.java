package com.example.filemanager.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.Formatter;
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
import com.example.filemanager.model.Application;
import com.example.filemanager.model.Image;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.Viewholder> {

    private ArrayList<Application> applications;
    private Context context;
    private OnItemClickListener callback;

    public ApplicationAdapter(ArrayList<Application> applications, Context context, OnItemClickListener callback) {
        this.applications = applications;
        this.context = context;
        this.callback = callback;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView iv_img_app;
        private TextView tv_name_app;
        private TextView tv_packageName_app;
        private TextView tv_size_app;

        public Viewholder(View itemView) {
            super(itemView);
            iv_img_app = itemView.findViewById(R.id.item_image_app);
            tv_name_app = itemView.findViewById(R.id.item_name_app);
            tv_packageName_app = itemView.findViewById(R.id.item_packageName_app);
            tv_size_app = itemView.findViewById(R.id.item_size_app);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ApplicationAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationAdapter.Viewholder holder, int position) {
        Application application = applications.get(position);
        holder.tv_name_app.setText(application.getNameApp());
        holder.tv_packageName_app.setText(application.getPackageApp());
        holder.tv_size_app.setText(Formatter.formatShortFileSize(context, application.getSizeApp()));
        Glide.with(context).load(application.getImageApp())
                .into(holder.iv_img_app);

    }

    @Override
    public int getItemCount() {
        return applications.size();
    }


}
