package com.example.android.fallout76news;

import java.util.ArrayList;

public class News extends ArrayList {

    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mURL;
    private String mSection;

    public News(String title, String author, String date, String url, String section) {
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mURL = url;
        mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mURL;
    }

    public String getSection() {
        return mSection;
    }

}
