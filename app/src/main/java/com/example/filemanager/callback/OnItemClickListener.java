package com.example.filemanager.callback;

import android.content.pm.PackageManager;

import java.io.File;

public interface OnItemClickListener {
    void onClick(int position);
    void onLongClick(int position);
    void onClickMore(int position);
}
