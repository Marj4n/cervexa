package com.gizthon.camera.fragment;

import android.Manifest;
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
import com.gizthon.camera.adapter.PhotoAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhotoListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PhotoAdapter adapter;
    private List<File> photoFiles = new ArrayList<>();
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadPhotos();
                } else {
                    Toast.makeText(getContext(), "Storage permission is required!", Toast.LENGTH_SHORT).show();
                }
            });

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        adapter = new PhotoAdapter(photoFiles, this::shareImage);
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            loadPhotos();
        }

        return view;
    }

    private void loadPhotos() {
        File photoDir = new File(Environment.getExternalStorageDirectory(), "MergeCamera/Media/Photo/Screen");
        if (photoDir.exists() && photoDir.isDirectory()) {
            File[] files = photoDir.listFiles();
            if (files != null) {
                photoFiles.clear();

                // Filter hanya file gambar
                List<File> imageFiles = new ArrayList<>();
                for (File file : files) {
                    if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) {
                        imageFiles.add(file);
                    }
                }

                // Urutkan file berdasarkan tanggal terbaru
                Collections.sort(imageFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

                photoFiles.addAll(imageFiles);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(requireContext(), "com.gizthon.camera.fileprovider", file);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
