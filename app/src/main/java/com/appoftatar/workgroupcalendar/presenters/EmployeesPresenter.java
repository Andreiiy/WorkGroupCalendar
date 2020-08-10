package com.appoftatar.workgroupcalendar.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.views.EmployesView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class EmployeesPresenter {

    private EmployesView view;
private DatabaseReference rootDataBase;

    @Inject
    public EmployeesPresenter(EmployesView view) {
        this.view = view;

    }

    public void getListEmployeesFromDataBase(){
        view.showProgressBar();
        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("users").orderByChild("IdWorkGroup").equalTo(Common.currentGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              ArrayList<User>  lEmployes = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.IdManager.equals(Common.currentUser.ID))
                        lEmployes.add(user);

                }
                if(lEmployes.size()!=0) {
                    view.hideProgressBar();
                    view.showEmployes(lEmployes);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

}
