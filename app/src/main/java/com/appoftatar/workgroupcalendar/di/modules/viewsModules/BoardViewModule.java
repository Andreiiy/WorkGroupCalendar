package com.appoftatar.workgroupcalendar.di.modules.viewsModules;

import com.appoftatar.workgroupcalendar.views.BoardView;

import dagger.Module;
import dagger.Provides;

@Module
public class BoardViewModule {

    private BoardView view;

    public BoardViewModule(BoardView view) {
        this.view = view;
    }

    @Provides
    public BoardView provideBordView(){
       return this.view;
    }
}
