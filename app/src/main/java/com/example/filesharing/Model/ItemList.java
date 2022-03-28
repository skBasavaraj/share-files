package com.example.filesharing.Model;

import android.net.Uri;

import java.util.List;

public class  ItemList {
    public String name;
      public long size;
     public String Path;
 // public  String progress;
 public   List<ItemList> list;
    public ItemList(String name, long size,String uri
    ) {
        this.name = name;
         this.size = size;
        this.Path=uri;
       // this.progress=progress;
     }

    public ItemList(List<ItemList> list) {
     this.list=list;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return Path;
    }
//    public String getProgress() {
//        return progress;
//    }
//
//    public void setProgress(String progress) {
//        this.progress = progress;
//    }
}
