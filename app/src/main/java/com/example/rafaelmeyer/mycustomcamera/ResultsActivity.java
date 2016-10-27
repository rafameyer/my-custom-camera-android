package com.example.rafaelmeyer.mycustomcamera;

import android.app.ActivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

public class ResultsActivity extends AppCompatActivity {

    private String path = "/storage/emulated/0/Android/data/com.example.rafaelmeyer.mycustomcamera/files/Pictures/";
    private File mGalleryFolder = new File(path);

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerVIew);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new MyAdapter(mGalleryFolder);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deleteAll:
                try {
                    deleteAllFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllFiles() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("pm clear com.example.rafaelmeyer.mycustomcamera");
/*        File dir = new File(getExternalFilesDirs(Environment.DIRECTORY_PICTURES).toString());
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File image : dir.listFiles()) {
                    deleteFile(image.toString());
                }
            deleteFile(dir.toString());
            }
        }*/
    }
}
