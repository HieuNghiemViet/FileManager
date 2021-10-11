package com.example.filemanager.view.custom;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.filemanager.R;
import com.example.filemanager.adapter.DocumentAdapter;
import com.example.filemanager.animation.MoveAnimation;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Document;
import com.example.filemanager.util.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DocumentStorageView extends RelativeLayout implements OnItemClickListener {
    private String documentName;
    private int documentSize;
    private URL documentUrl;
    private ArrayList<Document> arrayList = new ArrayList<>();
    private DocumentAdapter adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private Document document;

    public DocumentStorageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public DocumentStorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.document_storage_view, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_document_view);
    }

    private void initData() throws IOException {
        arrayList.clear();
        Search_Dir(Environment.getExternalStorageDirectory());
        setDataAdapter();
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
                    try {
                        initData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void setDataAdapter() {
        adapter = new DocumentAdapter(arrayList, mContext, this);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public void Search_Dir(File dir) throws IOException {
        File FileList[] = dir.listFiles();
        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {
                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    documentUrl = FileList[i].toURL();
                    URLConnection urlConnection = documentUrl.openConnection();
                    urlConnection.connect();
                    if (FileList[i].getName().endsWith(".pdf")) {
                        documentName = FileList[i].getName();
                        documentSize = urlConnection.getContentLength();
                        arrayList.add(new Document(documentName, documentSize, R.drawable.ic_pdf));

                    } else if (FileList[i].getName().endsWith(".docx")) {
                        documentName = FileList[i].getName();
                        documentSize = urlConnection.getContentLength();
                        arrayList.add(new Document(documentName, documentSize, R.drawable.ic_docx_file_format_symbol));

                    } else if (FileList[i].getName().endsWith(".doc")) {
                        documentName = FileList[i].getName();
                        documentSize = urlConnection.getContentLength();
                        arrayList.add(new Document(documentName, documentSize, 0));

                    } else if (FileList[i].getName().endsWith(".txt")) {
                        documentName = FileList[i].getName();
                        documentSize = urlConnection.getContentLength();
                        arrayList.add(new Document(documentName, documentSize, R.drawable.ic_txt_file_symbol));
                    }
                }
            }
        }
    }

    @Override
    public void onClick(int position) {
        document = arrayList.get(position);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        mContext.startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
    }

}
