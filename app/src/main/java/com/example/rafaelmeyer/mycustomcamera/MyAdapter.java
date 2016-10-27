package com.example.rafaelmeyer.mycustomcamera;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by rafael.meyer on 10/26/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private File imagesFile;

    public File getImagesFile() {
        return imagesFile;
    }

    public MyAdapter(File imagesFile) {
        this.imagesFile = imagesFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageFile = new File(getImagesFile().listFiles()[position].toURI());
        Picasso.with(holder.itemView.getContext()).load(imageFile).resize(250,250).centerCrop().into(holder.imageViewItem);
    }

    @Override
    public int getItemCount() {
        if (getImagesFile().listFiles() == null){
            return 0;
        }
        return getImagesFile().listFiles().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewItem = (ImageView) itemView.findViewById(R.id.imageViewItem);
        }
    }
}