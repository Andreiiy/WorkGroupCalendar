package com.appoftatar.workgroupcalendar.di.components;

import com.appoftatar.workgroupcalendar.di.modules.PresentersModule;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.BoardViewModule;
import com.appoftatar.workgroupcalendar.ui.board.BoardFragment;

import dagger.Component;

@Component(modules = {PresentersModule.class, BoardViewModule.class})
public interface BoardComponent {
    void inject(BoardFragment fragment);
}
