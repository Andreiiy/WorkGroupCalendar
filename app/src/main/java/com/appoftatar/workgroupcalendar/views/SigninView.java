package com.appoftatar.workgroupcalendar.views;

public interface SigninView {
    void showProgressBar();
    void hideProgressBar();
    void onLoginFailed();
    void onLoginSuccess();
    void emailTextError(Boolean checked);
    void passwordTextError(Boolean checked);

}
