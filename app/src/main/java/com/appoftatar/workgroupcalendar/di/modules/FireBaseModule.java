package com.appoftatar.workgroupcalendar.di.modules;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class FireBaseModule {

    @Provides
    public FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @Provides
    public DatabaseReference provideDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
