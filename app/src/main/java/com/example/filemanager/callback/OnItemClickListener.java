package com.example.filemanager.callback;

import android.content.pm.PackageManager;

public interface OnItemClickListener {
    void onClick(int position) throws PackageManager.NameNotFoundException;
    void onLongClick(int position);
}
