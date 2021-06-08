package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.filemanager.model.Image;

public class DetailImageActivity extends AppCompatActivity {
    private ImageView iv_image;
    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        iv_image = (ImageView) findViewById(R.id.iv_image_detail);
        image = (com.example.filemanager.model.Image) getIntent().getSerializableExtra("image");

        Glide.with(this)
                .load(image.getPath())
                .into(iv_image);
    }
}