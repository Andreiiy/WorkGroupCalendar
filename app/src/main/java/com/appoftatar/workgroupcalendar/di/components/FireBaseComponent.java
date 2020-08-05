package com.appoftatar.workgroupcalendar.di.components;


import com.appoftatar.workgroupcalendar.di.modules.FireBaseModule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import dagger.Component;
import dagger.Provides;

@Component(modules = FireBaseModule.class)
public interface FireBaseComponent {


    FirebaseAuth getFirebaseAuth();

    DatabaseReference getDatabaseReference();
}
