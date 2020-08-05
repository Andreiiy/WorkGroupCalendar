package com.appoftatar.workgroupcalendar.ui.board;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.BoardAdapter;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;
import com.appoftatar.workgroupcalendar.CreateMsgToBoardActivity;
import com.appoftatar.workgroupcalendar.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class BoardFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView listMessages;
    private DatabaseReference rootDataBase;
    ArrayList<MsgOnBoard> lMessages;
    private boolean actionActivity = true;
    private NotificationManager notificationManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_board, container, false);
        listMessages = (RecyclerView)root.findViewById(R.id.rvBoard);
        getListMessagesFromDataBase();

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , CreateMsgToBoardActivity.class);
                intent.putExtra("NAME_GROUP", Common.currentGroup);
                startActivity(intent);

            }
        });

        return root;
    }

    private void getListMessagesFromDataBase() {
        lMessages = new ArrayList<>();
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
                lMessages.clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    MsgOnBoard message = userSnapshot.getValue(MsgOnBoard.class);
                    if(message!=null) {
                        lMessages.add(message);
                }

                }
                if(lMessages.size() > 0 )
                    Collections.reverse(lMessages);
                createListMessages(lMessages);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public static BoardFragment getInstance(){
        Bundle args = new Bundle();
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void createListMessages(ArrayList<MsgOnBoard> listGroup){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);


        //put gridManager to recyclerview
        listMessages.setLayoutManager(gridLayoutManager);
        listMessages.setHasFixedSize(true);

        //create new adapter
       BoardAdapter messagesListAdapter = new BoardAdapter(listGroup);

        //put adapter to recyclerview
        listMessages.setAdapter(messagesListAdapter);


    }

    }

