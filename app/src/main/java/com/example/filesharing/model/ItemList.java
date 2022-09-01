package com.example.filesharing.model;

import java.util.List;

public class  ItemList {
      public String name;
      public long size;
      public String Path;
      public List<ItemList> list;

    public ItemList(String name, long size,String uri) {
        this.name = name;
         this.size = size;
        this.Path=uri;
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

}
