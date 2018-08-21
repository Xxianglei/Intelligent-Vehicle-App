package com.example.heath.presenter;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.heath.Datebase.ConnectModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Model.Contact;
import com.example.heath.view.IContactView;

import java.util.ArrayList;
import java.util.List;


public class ContactPresenter {

    private Context context;

    private IContactView iContactView;

    private AsyncQueryHandler aueryHandler;

    private Handler handler;

    private String key;

    public ContactPresenter(Context context,IContactView iContactView){
        this.context = context;
        this.iContactView = iContactView;

    }


    public void loadListContact(){
        if(aueryHandler == null){
            aueryHandler = new AsyncQueryHandler(context.getContentResolver()) {
                @Override
                public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                    super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);

                }

                @Override
                protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                    super.onQueryComplete(token, cookie, cursor);
                    List<Contact> list = testData();
                    iContactView.getListContact(list);
                }
            };
        }

    }

    private List<Contact> testData(){
        DataBaseManager dataBaseManager=new DataBaseManager();
        List<Contact> list = new ArrayList<>();
        List<ConnectModle> list1 = dataBaseManager.readconnList();
        if (list1.size() >= 1) {
            for (int i = list1.size() - 1; i >= 0; i--) {
                Contact c = new Contact();
                c.setName(list1.get((i)).getName());
                c.setNumber(list1.get((i)).getPhone());
                list.add(c);
                Log.e("电话***", list1.get((i)).getName() + list1.get((i)).getPhone());
            }
        }

        return list;
    }

    public void showLetter(String letter){
        this.key = letter;
        if(handler == null){
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            iContactView.showLetter(key);
                            handler.removeMessages(2);
                            handler.sendEmptyMessageDelayed(2,500);
                            break;
                        case 2:
                            iContactView.hideLetter();
                            break;
                    }
                }
            };
        }
        handler.sendEmptyMessage(1);

    }



}
