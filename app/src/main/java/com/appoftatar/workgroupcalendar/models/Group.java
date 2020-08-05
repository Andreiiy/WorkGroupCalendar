package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Group {

    public String idManager;
    public String nameGroup;

    public Group() {
    }

    public Group(String nameGroup,String idManager) {

        this.idManager = idManager;
        this.nameGroup = nameGroup;
    }
}
