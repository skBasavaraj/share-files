package com.example.filesharing.model;

public class ListItem {
String Lname;
String Lpath;

    public ListItem(String lname, String lpath) {

        Lname = lname;
        Lpath = lpath;
    }

    public String getLname() {
        return Lname;
    }

    public String getLpath() {
        return Lpath;
    }
}
