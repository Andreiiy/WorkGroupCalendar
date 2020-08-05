package com.appoftatar.workgroupcalendar.di.modules;

import com.appoftatar.workgroupcalendar.presenters.SigninPresenter;
import com.appoftatar.workgroupcalendar.views.SigninView;

import dagger.Module;
import dagger.Provides;

@Module(includes = SigninViewModule.class)
public class PresentersModule {

    @Provides
    public SigninPresenter provideSigninPresenter(SigninView view){
        return new SigninPresenter(view);
    }
}
