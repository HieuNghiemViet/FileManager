package com.example.filemanager.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filemanager.R;
import com.example.filemanager.model.Document;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private ArrayList<Document> documents;
    private Context context;

    public DocumentAdapter(ArrayList<Document> documents, Context context) {
        this.documents = documents;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_document;
        private TextView tv_documentName;
        private TextView tv_documentSize;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_document = itemView.findViewById(R.id.iv_document);
            tv_documentName = itemView.findViewById(R.id.tv_name_document);
            tv_documentSize = itemView.findViewById(R.id.tv_size_document);

        }
    }

    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_document, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.tv_documentName.setText(document.getDocumentName());
        holder.tv_documentSize.setText(Formatter.formatShortFileSize(context, document.getDocumentSize()));

        Glide.with(context)
                .load(document.getDocumentImage())
                .into(holder.iv_document);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }


}
