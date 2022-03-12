package com.example.filesharing.Model;

import android.graphics.drawable.Drawable;

public class App {
    public String Aname;
    public Drawable icon;
    public  String Apath;

    public App(String aname, Drawable icon, String apath) {
       this. Aname = aname;
        this.icon = icon;
        this.Apath = apath;
    }



    public String getAname() {
        return Aname;
    }


    public Drawable getIcon() {
        return icon;
    }

    public String getApath() {
        return Apath;
    }
}
