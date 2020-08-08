package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.SigninActivity;
import com.appoftatar.workgroupcalendar.di.modules.FireBaseModule;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.SigninViewModule;
import com.appoftatar.workgroupcalendar.presenters.SigninPresenter;

import dagger.Component;

@Component(modules ={ PresentersModule.class,SigninViewModule.class})
public interface SigninComponent {
     //SigninPresenter getSigninPresenter();
     void inject(SigninActivity activity);
}
