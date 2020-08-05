package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class MsgOnBoard {

    public String text;
    public String date;
    public String nameGroup;
    public String nameAuthor;
    public String statusManager;
    public MsgOnBoard() {
    }

    public MsgOnBoard(String text, String date, String nameGroup, String statusManager, String nameAuthor) {

        this.text = text;
        this.date = date;
        this.nameGroup = nameGroup;
        this.statusManager = statusManager;
        this.nameAuthor = nameAuthor;
    }
}
