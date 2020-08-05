package com.appoftatar.workgroupcalendar.di.modules;

import com.appoftatar.workgroupcalendar.views.SigninView;

import dagger.Module;
import dagger.Provides;

@Module
public class SigninViewModule {

    private SigninView signinView;

    public SigninViewModule(SigninView signinView) {
        this.signinView = signinView;
    }

    @Provides
    public SigninView provideSigninView(){
        return this.signinView;
    }
}
