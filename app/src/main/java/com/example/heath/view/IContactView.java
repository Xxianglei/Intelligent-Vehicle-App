package com.example.heath.view;


import com.example.heath.Model.Contact;

import java.util.List;

public interface IContactView {

    void getListContact(List<Contact> list);

    void showLetter(String letter);

    void hideLetter();
}
