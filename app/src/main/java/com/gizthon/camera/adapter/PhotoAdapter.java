package com.gizthon.camera.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.gizthon.camera.R;
import com.gizthon.camera.activity.ZoomableImageActivity;

import java.io.File;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<File> photoFiles;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onShareClick(File file);
    }

    public PhotoAdapter(List<File> photoFiles, OnPhotoClickListener listener) {
        this.photoFiles = photoFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = photoFiles.get(position);
        Glide.with(holder.imageView.getContext()).load(file).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Uri imageUri = FileProvider.getUriForFile(holder.imageView.getContext(), "com.gizthon.camera.fileprovider", file);
            Intent intent = new Intent(holder.imageView.getContext(), ZoomableImageActivity.class);
            intent.setData(imageUri);
            holder.imageView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photoFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
