package com.gizthon.camera.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gizthon.camera.R;
import com.gizthon.camera.adapter.VideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoListFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<File> videoFiles = new ArrayList<>();
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadVideos();
                } else {
                    Toast.makeText(getContext(), "Storage permission is required!", Toast.LENGTH_SHORT).show();
                }
            });

    public static VideoListFragment newInstance() {
        return new VideoListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new VideoAdapter(videoFiles, this::shareVideo);
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            loadVideos();
        }

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadVideos() {
        File videoDir = new File(Environment.getExternalStorageDirectory(), "MergeCamera/Media/Video");
        if (videoDir.exists() && videoDir.isDirectory()) {
            File[] files = videoDir.listFiles();
            if (files != null) {
                videoFiles.clear();
                for (File file : files) {
                    if (file.getName().endsWith(".mp4") || file.getName().endsWith(".mkv") || file.getName().endsWith(".avi") || file.getName().endsWith(".mov")) {
                        videoFiles.add(file);
                    }
                }
                Collections.sort(videoFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void shareVideo(File file) {
        Uri uri = FileProvider.getUriForFile(requireContext(), "com.gizthon.camera.fileprovider", file);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }
}