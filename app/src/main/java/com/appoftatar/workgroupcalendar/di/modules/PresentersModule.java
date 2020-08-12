package com.appoftatar.workgroupcalendar.di.modules;

import com.appoftatar.workgroupcalendar.di.modules.viewsModules.BoardViewModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.EmployeesModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.GroupsViewModule;
import com.appoftatar.workgroupcalendar.presenters.BoardPresenter;
import com.appoftatar.workgroupcalendar.presenters.EmployeesPresenter;
import com.appoftatar.workgroupcalendar.presenters.GroupsPresenter;
import com.appoftatar.workgroupcalendar.presenters.SigninPresenter;
import com.appoftatar.workgroupcalendar.presenters.SignupPresenter;
import com.appoftatar.workgroupcalendar.views.BoardView;
import com.appoftatar.workgroupcalendar.views.EmployesView;
import com.appoftatar.workgroupcalendar.views.GroupsView;
import com.appoftatar.workgroupcalendar.views.SigninView;
import com.appoftatar.workgroupcalendar.views.SignupView;

import dagger.Module;
import dagger.Provides;

@Module(includes = {SigninViewModule.class,SignupViewModule.class, GroupsViewModule.class, EmployeesModule.class,
        BoardViewModule.class})
public class PresentersModule {

    @Provides
    public SigninPresenter provideSigninPresenter(SigninView view){
        return new SigninPresenter(view);
    }

    @Provides
    public SignupPresenter provideSignupPresenter(SignupView view){
        return new SignupPresenter(view);
    }

    @Provides
    public GroupsPresenter provideGroupsPresenter(GroupsView view){
        return new GroupsPresenter(view);
    }
    @Provides
    public EmployeesPresenter provideEmployeesPresenter(EmployesView view){
        return new EmployeesPresenter(view);
    }

    @Provides
    public BoardPresenter provideBoardPresenter(BoardView view){
        return new BoardPresenter(view);
    }
}
