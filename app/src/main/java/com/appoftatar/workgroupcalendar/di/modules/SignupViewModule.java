package com.appoftatar.workgroupcalendar.di.modules;

import com.appoftatar.workgroupcalendar.views.SigninView;
import com.appoftatar.workgroupcalendar.views.SignupView;

import dagger.Module;
import dagger.Provides;

@Module
public class SignupViewModule {
    private SignupView signupView;

    public SignupViewModule(SignupView signupView) {
        this.signupView = signupView;
    }

    @Provides
    public SignupView provideSignupView(){
        return this.signupView;
    }
}
