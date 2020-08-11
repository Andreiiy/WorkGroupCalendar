package com.appoftatar.workgroupcalendar.views;

import com.appoftatar.workgroupcalendar.models.MsgOnBoard;

import java.util.ArrayList;

public interface BoardView {
    void showProgressBar();
    void hideProgressBar();
    void showListMessages(ArrayList<MsgOnBoard> listGroup);
}
