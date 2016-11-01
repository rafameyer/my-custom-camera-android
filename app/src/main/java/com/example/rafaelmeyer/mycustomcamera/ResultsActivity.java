package com.example.rafaelmeyer.mycustomcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements MyAdapter.OnPassFirstSelected, MyAdapter.OnPassSelected {

    private String path = "/storage/emulated/0/Android/data/com.example.rafaelmeyer.mycustomcamera/files/Pictures/";
    private File mGalleryFolder = new File(path);

    private List<Item> itemList = new ArrayList<>();

    private MyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Toolbar toolbar;
    private TextView textViewLabel;
    private ImageView imageViewDeleteToolbar;
    private ImageView imageViewBackArroyToolbar;
    private boolean isFirstActivity = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstActivity) {
            imageViewDeleteToolbar.setVisibility(View.VISIBLE);
        } else {
            imageViewDeleteToolbar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String titleToolbar = "Gallery";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageViewBackArroyToolbar = (ImageView) findViewById(R.id.imageViewBackArroyToolbar);
        imageViewBackArroyToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        textViewLabel = (TextView) findViewById(R.id.textViewTitleToolbar);
        textViewLabel.setText(titleToolbar);

        imageViewDeleteToolbar = (ImageView) findViewById(R.id.imageViewDeleteToolbar);
        imageViewDeleteToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAllItemsSelected();
                    }
                }
        );

        if (isFirstActivity) {

        }

        for (int i = 0; i < mGalleryFolder.listFiles().length; i++) {
            String file = mGalleryFolder.listFiles()[i].toString();
            Item item = new Item(file, false);
            itemList.add(item);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerVIew);
        mAdapter = new MyAdapter(mGalleryFolder, itemList);
        layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMyOnPassFirstSelected(this);
        mAdapter.setMyOnPassSelected(this);
    }

    private void deleteAllItemsSelected() {
        int size = mGalleryFolder.listFiles().length - 1;
        for (int i = size; i >= 0 ; i--) {
            if (itemList.get(i).getSelected()) {
                File fileModel = mGalleryFolder.listFiles()[i];
                fileModel.delete();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onPassSelected() {
        onResume();
    }

    @Override
    public void onPassFirstSelected(Boolean isFist) {
        Log.d("Results",isFist.toString());
        isFirstActivity = isFist;
        onResume();
    }
}