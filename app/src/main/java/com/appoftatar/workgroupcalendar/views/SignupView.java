package com.appoftatar.workgroupcalendar.views;

public interface SignupView {
    void registrationSuccess();
    void registrationFailed();
    void showErrorToField(String fieldName);
    void deleteErrorField(String fieldName);

}
