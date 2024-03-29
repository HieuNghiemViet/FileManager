package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.filemanager.R;
import com.example.filemanager.activity.DetailImageActivity;
import com.example.filemanager.activity.MainActivity;
import com.example.filemanager.adapter.ImageAdapter;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Image;
import com.example.filemanager.util.ScreenUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.BuildConfig;

public class ImageStorageView extends RelativeLayout implements OnItemClickListener {
    private Context mContext;
    private static Uri extStorageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private static Uri extDownloadUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private RecyclerView recyclerView;
    private ArrayList<Image> arrayList = new ArrayList<>();
    private ImageAdapter adapter;
    private TextView tv_info_image_cancel;
    private TextView tv_name;
    private TextView tv_path;
    private TextView tv_size;
    private TextView tv_date;
    private TextView tv_resolution;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private EditText edt_rename;
    private Image imageTmp;
    private SwipeRefreshLayout swipe;

    public ImageStorageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ImageStorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.image_storage_view, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_image_view);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutImage);
    }

    private void initData() {
        setDataAdapter();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(mContext, DetailImageActivity.class);
        Image image = arrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", image);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        imageTmp = arrayList.get(position);
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(mContext);
        final String[] feature = {"Thông tin", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoImage(Gravity.CENTER, imageTmp);
                        break;
                    case 1:
                        renameImage(Gravity.CENTER, imageTmp);
                        break;
                    case 2:
                        deleteDialog(imageTmp);
                        break;
                    case 3:
                        shareImage(imageTmp);
                        break;
                }
            }
        });
        myBuilder.create().show();
    }

    public boolean isOpening() {
        return getVisibility() == VISIBLE;
    }

    public void openView() {
        if (getVisibility() == GONE) {
            MoveAnimation.openViewFromRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    initData();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    public void closeView() {

        MoveAnimation.closeViewToRight(this, ScreenUtils.getScreenWidth(mContext), 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setDataAdapter() {
        adapter = new ImageAdapter(arrayList, mContext, this);
        getImage();
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                setDataAdapter();
            }
        });
    }

    public void getImage() {
        arrayList.clear();
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri imgUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imgUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor imgCursor = contentResolver.query(imgUri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (imgCursor != null && imgCursor.moveToFirst()) {
            int imgTitle = imgCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int imgDisplay = imgCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int imgPath = imgCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int imgSize = imgCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int imgDate = imgCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            int imgId = imgCursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                String currentTitle = imgCursor.getString(imgDisplay);
                String currentPath = imgCursor.getString(imgPath);
                String currentDisplay = imgCursor.getString(imgDisplay);
                long currentSize = imgCursor.getLong(imgSize);
                long currentDate = imgCursor.getLong(imgDate);
                long currentID = imgCursor.getLong(imgId);
                arrayList.add(new Image(currentPath, currentTitle, currentSize, currentDate, currentDisplay, currentID));
            } while (imgCursor.moveToNext());
        }
    }

    private void renameImage(int gravity, Image image) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rename);

        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
        edt_rename = dialog.findViewById(R.id.edt_rename);
        edt_rename.setText(image.getTitle());

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_rename_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (image.getPath().contains("/storage/emulated/0/Download")) {
                        renameFileDownloadUsingDisplayName(mContext, image.getDisplayName());
                    } else {
                        renameFileStorageUsingDisplayName(mContext, image.getDisplayName());
                    }
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        tv_rename_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public boolean renameFileDownloadUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        try {
            Long id = getIdDownloadFromDisplayName(displayName);
            if(id == null){
                return false;
            }
            ContentResolver resolver = context.getContentResolver();
            Uri mUri = ContentUris.withAppendedId(extDownloadUri, id);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
            contentValues.clear();

            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, edt_rename.getText().toString());
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0);
            contentValues.put(MediaStore.Downloads.TITLE, edt_rename.getText().toString());
            resolver.update(mUri, contentValues, null, null);
            imageTmp.setTitle(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getImage();
            return true;
        } catch (SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException) securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender = recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                MainActivity.sMainActivity.startIntentSenderForResult(intentSender, MainActivity.RENAME_REQUEST_CODE,
                        null, 0, 0, 0, null);
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
        return false;
    }

    public Long getIdDownloadFromDisplayName(String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Cursor cursor = mContext.getContentResolver().query(extDownloadUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);
        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return fileId;
        }
        return null;
    }

    public boolean renameFileStorageUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        try {
            Long id = getIdStorageFromDisplayName(displayName);
            if(id == null){
                return false;
            }
            ContentResolver resolver = context.getContentResolver();
            Uri mUri = ContentUris.withAppendedId(extStorageUri, id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
            contentValues.clear();

            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, edt_rename.getText().toString());
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentValues.put(MediaStore.Images.Media.TITLE, edt_rename.getText().toString());
            resolver.update(mUri, contentValues, null, null);
            imageTmp.setTitle(edt_rename.getText().toString());
            adapter.notifyDataSetChanged();
            getImage();
            return true;
        } catch (SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException) securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender = recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                MainActivity.sMainActivity.startIntentSenderForResult(intentSender, MainActivity.RENAME_REQUEST_CODE, null, 0, 0, 0, null);
            } else {
                throw new RuntimeException(
                        securityException.getMessage(), securityException);
            }
        }
        return false;
    }

    public Long getIdStorageFromDisplayName(String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Cursor cursor = mContext.getContentResolver().query(extStorageUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);
        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return fileId;
        }
        return null;
    }


    public Uri getUriFromDisplayName(Context context, String displayName) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(imgUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{displayName}, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return Uri.parse(imgUri.toString() + "/" + fileId);
        } else {
            return null;
        }
    }


    public boolean deleteFileUsingDisplayName(Context context, String displayName) throws IntentSender.SendIntentException {
        Uri uri = getUriFromDisplayName(context, displayName);
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            String[] selectionArgsPdf = new String[]{displayName};

            try {
                if (resolver.delete(uri, MediaStore.Files.FileColumns.DISPLAY_NAME + "=?", selectionArgsPdf) > 0) {
                    arrayList.remove(imageTmp);
                    adapter.notifyDataSetChanged();

                }

                return true;
            } catch (SecurityException securityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    RecoverableSecurityException recoverableSecurityException;
                    if (securityException instanceof RecoverableSecurityException) {
                        recoverableSecurityException =
                                (RecoverableSecurityException) securityException;
                    } else {
                        throw new RuntimeException(
                                securityException.getMessage(), securityException);
                    }
                    IntentSender intentSender = recoverableSecurityException.getUserAction()
                            .getActionIntent().getIntentSender();
                    MainActivity.sMainActivity.startIntentSenderForResult(intentSender, MainActivity.DELETE_REQUEST_CODE,
                            null, 0, 0, 0, null);
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
            }
        }
        return false;
    }


    public void deleteDialog(Image image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete Image")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteFileUsingDisplayName(mContext, image.getDisplayName());
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void shareImage(Image image) {
        File imgFile = new File(image.getPath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", imgFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void infoImage(int gravity, Image image) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_image);

        tv_info_image_cancel = dialog.findViewById(R.id.tv_cancel_image);
        tv_name = dialog.findViewById(R.id.tv_name_image_main_storage);
        tv_path = dialog.findViewById(R.id.tv_path_main_image_storage);
        tv_size = dialog.findViewById(R.id.tv_size_image_main_storage);
        tv_date = dialog.findViewById(R.id.tv_dayImage);
        tv_resolution = dialog.findViewById(R.id.tv_resolutionImage);

        tv_name.setText(image.getDisplayName());
        tv_path.setText(image.getPath());
        tv_size.setText(Formatter.formatShortFileSize(dialog.getContext(), image.getSize()));

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        tv_date.setText(sdf.format(image.getDate() * 1000));

        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        bitmap.getHeight();
        bitmap.getWidth();
        tv_resolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_info_image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == DELETE_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    deleteFileUsingDisplayName(mContext, imageTmp.getDisplayName());
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if ((requestCode == RENAME_REQUEST_CODE)) {
//            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    renameFileDownloadUsingDisplayName(mContext, imageTmp.getDisplayName());
//                    renameFileStorageUsingDisplayName(mContext, imageTmp.getDisplayName());
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    public void deleteImage() {
        try {
            deleteFileUsingDisplayName(mContext, imageTmp.getDisplayName());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void renameImage() {
        try {
            renameFileStorageUsingDisplayName(mContext, imageTmp.getDisplayName());
            renameFileDownloadUsingDisplayName(mContext, imageTmp.getDisplayName());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}
