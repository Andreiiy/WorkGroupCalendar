package com.appoftatar.workgroupcalendar.views;

import com.appoftatar.workgroupcalendar.models.User;

import java.util.ArrayList;

public interface EmployesView {
    void showProgressBar();
    void hideProgressBar();
    void showEmployes(ArrayList<User> lEmployes);
}
