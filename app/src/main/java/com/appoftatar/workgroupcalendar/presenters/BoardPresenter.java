package com.appoftatar.workgroupcalendar.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;
import com.appoftatar.workgroupcalendar.views.BoardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class BoardPresenter {

    private BoardView view;
    private DatabaseReference rootDataBase;


    @Inject
    public BoardPresenter(BoardView view) {
        this.view = view;

    }

    public void getListMessages() {
        view.showProgressBar();
        rootDataBase = FirebaseDatabase.getInstance().getReference();
        String idManager = Common.currentUser.IdManager;
        String currentGroup = Common.currentUser.IdWorkGroup;
        if(Common.manager) {
            idManager = Common.currentUser.ID;
            currentGroup = Common.currentGroup;
        }
        rootDataBase.child("boards").child(idManager).orderByChild("nameGroup").equalTo(currentGroup).limitToLast(15).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<MsgOnBoard> lMessages = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    MsgOnBoard message = userSnapshot.getValue(MsgOnBoard.class);
                    if(message!=null) {
                        lMessages.add(message);
                    }

                }
                if(lMessages.size() > 0 )
                    Collections.reverse(lMessages);
                view.hideProgressBar();
                view.showListMessages(lMessages);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
