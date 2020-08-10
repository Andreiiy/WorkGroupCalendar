package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.SigninActivity;
import com.appoftatar.workgroupcalendar.SignupActivity;
import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.SigninViewModule;
import com.appoftatar.workgroupcalendar.di.modules.SignupViewModule;

import dagger.Component;

@Component(modules ={ PresentersModule.class, SigninViewModule.class, SignupViewModule.class})
public interface EntranceComponent {

        void inject(SigninActivity activity);
        void inject(SignupActivity activity);
    }

