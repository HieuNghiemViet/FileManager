package com.example.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.filemanager.R;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Video;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private ArrayList<Video> videos;
    private Context context;
    private OnItemClickListener callback;

    public VideoAdapter(ArrayList<Video> videos, Context context, OnItemClickListener callback) {
        this.videos = videos;
        this.context = context;
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_video;
        private TextView tv_video;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_video = itemView.findViewById(R.id.iv_item_video);
            tv_video = itemView.findViewById(R.id.tv_item_nameVideo);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.tv_video.setText(video.getNameVideo());

        Glide.with(context)
                .load(video.getPathVideo())
                .into(holder.iv_video);


        holder.iv_video.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onLongClick(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


}
