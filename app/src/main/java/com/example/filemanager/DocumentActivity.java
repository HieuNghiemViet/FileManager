package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filemanager.adapter.DocumentAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Formatter;

public class DocumentActivity extends AppCompatActivity implements OnItemClickListener {
    private String documentName;
    private int documentSize;
    private URL documentUrl;
    private ImageView documentImage;
    private ArrayList<Document> arrayList = new ArrayList<>();
    private DocumentAdapter adapter;
    private RecyclerView recyclerView;
    private Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        try {
            Search_Dir(Environment.getExternalStorageDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initView();
        setDataAdapter();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_document);
    }

    public void setDataAdapter() {
        adapter = new DocumentAdapter(arrayList, this, (OnItemClickListener) this);
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
     //   document = arrayList.get(position);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivity(intent);

    }

    @Override
    public void onLongClick(int position) {
    }
}