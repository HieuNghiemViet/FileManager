package com.example.filemanager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipManager {
    private final int BUFFER = 4000;

    public void zipFile(String[] files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < files.length; i++) {
                Log.d("HieuNV", "files.length: " + files.length);
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                Log.d("HieuNV", "entry: " + entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                    Log.d("HieuNV", "data: " + data.length);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean extractFileZip(Context context, String pathFileZip, String pathFolderSave) {
        File zipFile = new File(pathFileZip);
        File targetDirectory = new File(pathFolderSave);
        targetDirectory.mkdirs();
        ZipInputStream zis;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    fout.close();
                }
                scanFile(context, file.getPath(), false);
            }

            Log.e("extractFileZip", "COMPLETE");
            zis.close();
            //  deleteRecursive(new File(pathFileZip));
        } catch (Exception e) {
            //  deleteRecursive(new File(pathFileZip));
            e.printStackTrace();
            Log.e("extractFileZip", "EROR");
            return false;
        }
        return true;
    }




    public void scanFile(final Context context, String pathFile, final boolean isPushNotify) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{pathFile}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
