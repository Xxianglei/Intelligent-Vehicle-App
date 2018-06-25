package com.example.heath.view;


public class CardItem {

    private String mTextResource;
    private int mTitleResource;

    public CardItem(int title, String text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public String getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}
