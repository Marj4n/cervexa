package com.gizthon.camera.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.gizthon.camera.R;
import java.io.File;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<File> videoFiles;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onShareClick(File file);
    }

    public VideoAdapter(List<File> videoFiles, OnVideoClickListener listener) {
        this.videoFiles = videoFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = videoFiles.get(position);
        Glide.with(holder.imageView.getContext()).load(file).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Uri videoUri = FileProvider.getUriForFile(holder.imageView.getContext(), "com.gizthon.camera.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(videoUri, "video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                holder.imageView.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(holder.imageView.getContext(), "No application found to play this video", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageView.setOnLongClickListener(v -> {
            // Share video
            listener.onShareClick(file);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}