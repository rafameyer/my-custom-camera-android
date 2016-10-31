package com.example.rafaelmeyer.mycustomcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;

public class ResultsActivity extends AppCompatActivity implements MyAdapter.RecyclerViewOnClickListener {

    private String path = "/storage/emulated/0/Android/data/com.example.rafaelmeyer.mycustomcamera/files/Pictures/";
    private File mGalleryFolder = new File(path);

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerVIew);
        layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(mGalleryFolder);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setMyRecyclerViewOnClickListener(this);
    }

    @Override
    public void onClickListenerToDelete(View view, File imageFile) {
        Log.d("Results","Click Item");
        imageFile.delete();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }
}
