package com.example.filemanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.model.Folder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipManager {
    private final int BUFFER = 8192;
    private ArrayList<String> arrayListZip = new ArrayList();
    public Folder folderTmp;
    private Context context;
    private ProgressDialog progressDialog;
    private CallBackZipListener listener;
    private int hCount = 0;
    private boolean continueZipFile = true;
    private String unzipPath;
    private StorageAdapter adapter;
    private String path;

    public ZipManager(Context context, CallBackZipListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void zipFile(Folder folderTmp) {
        this.folderTmp = folderTmp;
        new MyAsyncTaskZip().execute();
        showCompressingProgressDialog();
    }

    public void unZipFile(Folder folderTmp, String desPath) {
        this.folderTmp = folderTmp;
        this.unzipPath = desPath;
        new MyAsyncTaskUnZip().execute();
        showExtractProgressDialog();
    }

    private void showExtractProgressDialog() {
        progressDialog = new ProgressDialog(context);
        File file = new File(folderTmp.getPathFolder());
        long size = getFolderSize(file);
        progressDialog.setProgressNumberFormat(Formatter.formatShortFileSize(context, size));
        if (size > 1024 * 1024) {
            size = size / 1024;
        } else if (size > 1024 * 1024 * 1024) {
            size = size / 1024 / 1024;
        }

        progressDialog.setProgress(0);
        progressDialog.setMax((int) size);

        progressDialog.setMessage("Extracting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void showCompressingProgressDialog() {
        progressDialog = new ProgressDialog(context);
        File file = new File(folderTmp.getPathFolder());
        long size = getFolderSize(file);
        progressDialog.setProgressNumberFormat(Formatter.formatShortFileSize(context, size));
        if (size > 1024 * 1024) {
            size = size / 1024;
        } else if (size > 1024 * 1024 * 1024) {
            size = size / 1024 / 1024;
        }

        progressDialog.setProgress(0);
        progressDialog.setMax((int) size);
        progressDialog.setMessage("Compressing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private class MyAsyncTaskZip extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            arrayListZip.clear();

            getListPathToZip(folderTmp.getPathFolder());
            String[] files = new String[arrayListZip.size()];
            arrayListZip.toArray(files);

            try {
                BufferedInputStream origin = null;
                FileOutputStream dest = new FileOutputStream(folderTmp.getPathFolder() + ".zip");
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                        dest));
                byte data[] = new byte[BUFFER];
                for (int i = 0; i < files.length; i++) {
                    FileInputStream fi = new FileInputStream(files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);

                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;

                    while (continueZipFile && (count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                        hCount += count;
                        int value = hCount;
                        if (value > 1024 * 1024) {
                            value = value / 1024;
                        } else if (value > 1024 * 1024 * 1024) {
                            value = value / 1024 / 1024;
                        }
                        publishProgress(value);
                    }
                    hCount = 0;
                    origin.close();
                }
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int number = values[0];
            progressDialog.setProgress(number);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            listener.OnZipComplete();
        }

    }

    private class MyAsyncTaskUnZip extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            File zipFile = new File(folderTmp.getPathFolder());
            File targetDirectory = new File(unzipPath);

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
                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                            hCount += count;
                            int value = hCount;
                            if (value > 1024 * 1024) {
                                value = value / 1024;
                            } else if (value > 1024 * 1024 * 1024) {
                                value = value / 1024 / 1024;
                            }
                            publishProgress(value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        hCount = 0;
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
                Log.e("extractFileZip", "ERROR");
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int number = values[0];
            progressDialog.setProgress(number);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            listener.OnUnZipComplete();
        }
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

    public void getListPathToZip(String path) {
        File src = new File(path);

        String files[] = src.list();
        if (files != null) {
            if (files.length > 0) {
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    files[i] = path + "/" + files[i];
                }
                for (int i = 0; i < filesLength; i++) {
                    File f = new File(files[i]);
                    if (f.isDirectory()) {
                        getListPathToZip(files[i]);
                    } else {
                        arrayListZip.add(files[i]);
                    }
                }
            }
        } else {
            if (src != null) {
                arrayListZip.add(src.getAbsolutePath());
            }
        }
    }

    public long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }
}

interface CallBackZipListener {
    void OnZipComplete();

    void OnUnZipComplete();
}
