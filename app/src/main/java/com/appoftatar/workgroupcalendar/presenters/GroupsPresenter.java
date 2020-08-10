package com.appoftatar.workgroupcalendar.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.di.components.DaggerFireBaseComponent;
import com.appoftatar.workgroupcalendar.di.components.FireBaseComponent;
import com.appoftatar.workgroupcalendar.models.Group;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.views.GroupsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class GroupsPresenter {

    private GroupsView view;
    FireBaseComponent fireBaseComponent;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootDataBase;

    @Inject
    public GroupsPresenter(GroupsView view) {
        this.view = view;
        fireBaseComponent = DaggerFireBaseComponent.builder().build();

    }

    private  void getCurrentUser(){
        mAuth = fireBaseComponent.getFirebaseAuth();
        firebaseUser = mAuth.getInstance().getCurrentUser();
        rootDataBase =fireBaseComponent.getDatabaseReference();

        rootDataBase.child("users").orderByChild("ID").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user!=null) {
                        if(Common.manager && user.Manager.equals("true")) {
                            Common.currentUser = user;
                            view.showUserName(user.FirstName + " " + user.SurName);
                            getGroups();
                        }else {
                            view.userLogout();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

        private   void getGroups(){

            rootDataBase.child("groups").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Group> listGroup = new ArrayList<>();
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        Group group = userSnapshot.getValue(Group.class);
                        if(group!=null) {
                            listGroup.add(group);
                        }
                    }
                    if(listGroup.size()!=0) {
                        Common.listGroup = listGroup;
                        view.hideProgressBar();
                        view.showGroups(listGroup);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Data base", "Failed to read value.", databaseError.toException());
                }
            });

    }

    public void showGroups(){
        view.showProgressBar();
        getCurrentUser();
    }

}
