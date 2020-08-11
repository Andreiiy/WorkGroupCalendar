package com.appoftatar.workgroupcalendar.ui.board;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import com.appoftatar.workgroupcalendar.CreateGroupActivity;
import com.appoftatar.workgroupcalendar.adapters.BoardAdapter;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;
import com.appoftatar.workgroupcalendar.CreateMsgToBoardActivity;
import com.appoftatar.workgroupcalendar.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.views.BoardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class BoardFragment extends Fragment implements BoardView {

    private FloatingActionButton fab;
    private RecyclerView listMessages;
    private View root;
    private ProgressDialog progressDialog;






    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_board, container, false);

        initViews();

        //getListMessagesFromDataBase();


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

    private void initViews(){
        listMessages = (RecyclerView)root.findViewById(R.id.rvBoard);
        fab = root.findViewById(R.id.fab);

        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get Data...");
    }



    public static BoardFragment getInstance(){
        Bundle args = new Bundle();
        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void showProgressBar() {
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void showListMessages(ArrayList<MsgOnBoard> listGroup){

        //put gridManager to recyclerview
        listMessages.setLayoutManager(new GridLayoutManager(getContext(),1));
        listMessages.setHasFixedSize(true);

        //put adapter to recyclerview
        listMessages.setAdapter(new BoardAdapter(listGroup));

    }

    }

