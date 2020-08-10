package com.appoftatar.workgroupcalendar.views;

import com.appoftatar.workgroupcalendar.models.Group;

import java.util.ArrayList;

public interface GroupsView {
    void showProgressBar();
    void hideProgressBar();
    void userLogout();
    void showUserName(String name);
    void showGroups(ArrayList<Group> listGroup);
}
