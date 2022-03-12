package com.example.filesharing.Model;

import java.util.List;

public class  ItemList {
    public String name;
    public String path;
     public long size;
 // public  String progress;
 public   List<ItemList> list;
    public ItemList(String name, String path, long size
    ) {
        this.name = name;
        this.path = path;
        this.size = size;
       // this.progress=progress;
     }

    public ItemList(List<ItemList> list) {
     this.list=list;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

//    public String getProgress() {
//        return progress;
//    }
//
//    public void setProgress(String progress) {
//        this.progress = progress;
//    }
}
