package com.example.rafaelmeyer.mycustomcamera;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
    private ImageView imageViewBackArrowToolbar;
    private ImageView imageViewUnselectedToolbar;

    private boolean isFirstActivity = false;
    private int countSelected;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstActivity) {
            imageViewDeleteToolbar.setVisibility(View.VISIBLE);
            imageViewUnselectedToolbar.setVisibility(View.VISIBLE);
        } else if (countSelected == 0) {
            imageViewDeleteToolbar.setVisibility(View.INVISIBLE);
            imageViewUnselectedToolbar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String titleToolbar = "Gallery";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageViewBackArrowToolbar = (ImageView) findViewById(R.id.imageViewBackArrowToolbar);
        imageViewBackArrowToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        textViewLabel = (TextView) findViewById(R.id.textViewTitleToolbar);
        textViewLabel.setText(titleToolbar);

        imageViewUnselectedToolbar = (ImageView) findViewById(R.id.imageViewUnselectedToolbar);
        imageViewUnselectedToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItemSelected();
                    }
                }
        );

        imageViewDeleteToolbar = (ImageView) findViewById(R.id.imageViewDeleteToolbar);
        imageViewDeleteToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAllItemsSelected();
                    }
                }
        );

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

    private void removeItemSelected() {
        int size = mGalleryFolder.listFiles().length - 1;
        for (int i = size; i >= 0 ; i--) {
            if (itemList.get(i).getSelected()) {
                itemList.get(i).setSelected(false);
                mAdapter.setCountSelected(mAdapter.getCountSelected() - 1);
            }
        }
        countSelected = 0;
        mAdapter.isFirst = false;
        mAdapter.notifyDataSetChanged();
        imageViewUnselectedToolbar.setVisibility(View.INVISIBLE);
        imageViewDeleteToolbar.setVisibility(View.INVISIBLE);
        textViewLabel.setText("Gallery");
    }

    private void deleteAllItemsSelected() {
        int size = mGalleryFolder.listFiles().length - 1;
        for (int i = size; i >= 0 ; i--) {
            if (itemList.get(i).getSelected()) {
                itemList.get(i).setSelected(false);
                File fileModel = mGalleryFolder.listFiles()[i];
                fileModel.delete();
                mAdapter.setCountSelected(mAdapter.getCountSelected() - 1);
            }
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.isFirst = false;
        textViewLabel.setText("Gallery");
        imageViewDeleteToolbar.setVisibility(View.INVISIBLE);
        imageViewUnselectedToolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onPassSelected(View view, File imageModel, Boolean isFirst, int count) {
        isFirstActivity = isFirst;
        countSelected = count;
        onResume();

        if (count == 0) {
            imageViewDeleteToolbar.setVisibility(View.INVISIBLE);
            imageViewUnselectedToolbar.setVisibility(View.INVISIBLE);
        }

        if (count == 0) {
            textViewLabel.setText("Gallery");
        }
        if (count > 0){
            textViewLabel.setText(countSelected + " Selected");
        }

        if (!isFirst && count == 0) {
            String transition = "photo";

            View viewStart = view.findViewById(R.id.imageViewItem);

            Intent intent = new Intent(ResultsActivity.this, PhotoActivity.class);
            ActivityOptionsCompat optionsCompat
                    = ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewStart, transition);

            intent.putExtra("image", imageModel);

            ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewStart, transition);
            ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        }
    }

    @Override
    public void onPassFirstSelected(Boolean isFist, int count) {
        Log.d("Results",isFist.toString());
        isFirstActivity = isFist;
        countSelected = count;

        if (count == 0) {
            textViewLabel.setText("Gallery");
        }
        if (count > 0){
            textViewLabel.setText(count + " Selected");
        }

        onResume();
    }
}