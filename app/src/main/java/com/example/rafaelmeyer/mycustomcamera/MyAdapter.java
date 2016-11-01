package com.example.rafaelmeyer.mycustomcamera;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael.meyer on 10/26/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public File imagesFile;
    public Boolean isFirst = false;
    private int countSelected;
    private List<Item> itemList = new ArrayList<>();

    private OnPassSelected myOnPassSelected;
    private OnPassFirstSelected myOnPassFirstSelected;

    public Boolean getFirst() {
        return isFirst;
    }

    public File getImagesFile() {
        return imagesFile;
    }

    public MyAdapter(File imagesFile, List<Item> itemList) {
        this.imagesFile = imagesFile;
        this.itemList = itemList;
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
        if (itemList.get(position).getSelected()) {
            holder.cardViewItem.setBackgroundColor(Color.BLUE);
        } else {
            holder.cardViewItem.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (getImagesFile().listFiles() == null){
            return 0;
        }
        return getImagesFile().listFiles().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageViewItem;
        CardView cardViewItem;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewItem = (ImageView) itemView.findViewById(R.id.imageViewItem);
            cardViewItem = (CardView) itemView.findViewById(R.id.cardViewItem);
            cardViewItem.setOnLongClickListener(this);
            cardViewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isFirst) {
                if (!itemList.get(getAdapterPosition()).getSelected()) {
                    itemList.get(getAdapterPosition()).setSelected(true);
                    countSelected++;
                    if (myOnPassSelected != null) {
                        myOnPassSelected.onPassSelected();
                    }
                } else {
                    itemList.get(getAdapterPosition()).setSelected(false);
                    countSelected--;
                    if (countSelected == 0)
                    if (myOnPassSelected != null) {
                        myOnPassSelected.onPassSelected();
                    }
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (!isFirst) {
                isFirst = true;
                itemList.get(getAdapterPosition()).setSelected(true);
                if (myOnPassFirstSelected != null) {
                    myOnPassFirstSelected.onPassFirstSelected(isFirst);
                }
                notifyDataSetChanged();
                return true;
            } else if (countSelected == 0) {
                isFirst = false;
                itemList.get(getAdapterPosition()).setSelected(false);
                if (myOnPassFirstSelected != null) {
                    myOnPassFirstSelected.onPassFirstSelected(isFirst);
                }
                notifyDataSetChanged();
            }
            return false;
        }
    }

    public interface OnPassFirstSelected {
        void onPassFirstSelected(Boolean isFist);
    }

    public interface OnPassSelected {
        void onPassSelected();
    }

    public void setMyOnPassSelected(OnPassSelected myOnPassSelected) {
        this.myOnPassSelected = myOnPassSelected;
    }

    public void setMyOnPassFirstSelected(OnPassFirstSelected myOnPassFirstSelected) {
        this.myOnPassFirstSelected = myOnPassFirstSelected;
    }
}

