package com.example.filemanager.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
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
import com.example.filemanager.model.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> songs;
    private Context context;
    private OnItemClickListener callback;

    public SongAdapter(ArrayList<Song> songs, Context context, OnItemClickListener callback) {
        this.songs = songs;
        this.context = context;
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSong;
        private TextView tvName;
        private TextView tvArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSong = itemView.findViewById(R.id.iv_img_song);
            tvArtist = itemView.findViewById(R.id.tv_artist_song);
            tvName = itemView.findViewById(R.id.tv_name_song);
            tvName.setSelected(true);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvName.setText(song.getNameSong());
        holder.tvArtist.setText(song.getArtistSong());


        Glide.with(context)
                .load(getAlbumArtURI(song.getImageSong()))
                .placeholder(R.drawable.compact_disc)
                .into(holder.ivSong);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onLongClick(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static Uri getAlbumArtURI(Long album_id) {
        try {
            final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            return ContentUris.withAppendedId(sArtworkUri, album_id);
        } catch (Exception ignored) {
            return null;
        }
    }


}
