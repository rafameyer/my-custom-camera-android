package com.example.rafaelmeyer.mycustomcamera;

/**
 * Created by rafael.meyer on 11/1/16.
 */
public class Item {

    private String filePhoto;
    private Boolean isSelected;

    public Item(String filePhoto, Boolean isSelected) {
        this.filePhoto = filePhoto;
        this.isSelected = isSelected;
    }

    public String getFilePhoto() {
        return filePhoto;
    }

    public void setFilePhoto(String filePhoto) {
        this.filePhoto = filePhoto;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
