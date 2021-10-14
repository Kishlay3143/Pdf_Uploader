package com.example.pdfuploader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myAdapter extends FirebaseRecyclerAdapter<fileInfoModel,myAdapter.myViewholder> {

    public myAdapter(@NonNull final FirebaseRecyclerOptions<fileInfoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewholder holder, final int position, @NonNull final fileInfoModel model) {

        holder.fileTitle.setText(model.getFileName());
        holder.viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(holder.viewPdf.getContext(),viewPdf.class);
                intent.putExtra("fileName",model.getFileName());
                intent.putExtra("fileUrl",model.getFileUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.viewPdf.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder{

        ImageView viewPdf;
        TextView fileTitle;
        public myViewholder(@NonNull final View itemView) {
            super(itemView);
            viewPdf=itemView.findViewById(R.id.viewPdf);
            fileTitle=itemView.findViewById(R.id.fileTitle);
        }
    }

}
