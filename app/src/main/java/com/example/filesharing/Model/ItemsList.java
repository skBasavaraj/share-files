package com.example.filesharing.Model;

import android.net.Uri;

public class ItemsList {
    public String name;
    public Uri uri;
    public long size;
    public String date;
    public String path;
   boolean isSelected;
    public ItemsList(String name, Uri uri, long size, String date,String path) {
        this.name = name;
        this.uri = uri;
        this.size = size;
        this.date = date;
        this.path=path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public long getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }
}
