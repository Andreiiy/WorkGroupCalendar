package com.appoftatar.workgroupcalendar;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.GroupListAdapter;
import com.appoftatar.workgroupcalendar.models.Group;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {
    //region ============= FIELDS ===================//

    private RecyclerView listGroups;
    private TextView tvUserName;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootDataBase;
    private ArrayList<Group> listGroup;
    private GroupListAdapter groupListAdapter;
    private NotificationManager notificationManager;
    private FloatingActionButton fab;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //region ============= INITIALISATION VIEW ELEMENTS ===================//

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.listGroup = listGroup;
                Intent intent = new Intent(getApplicationContext() ,FormNewGroupActivity.class);
                startActivity(intent);

            }
        });
        listGroup = new ArrayList<>();

        createListGroup(listGroup);

        firebaseUser = mAuth.getInstance().getCurrentUser();
        rootDataBase = FirebaseDatabase.getInstance().getReference();
        tvUserName = (TextView)findViewById(R.id.nameManager);
        //endregion

        getCurrentUser();

        //get all group
        getGroup();

   }

private  void getCurrentUser(){


    rootDataBase.child("users").orderByChild("ID").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                User user = userSnapshot.getValue(User.class);
                if(user!=null) {
                    if(Common.manager && user.Manager.equals("true")) {
                        Common.currentUser = user;
                        tvUserName.setText(user.FirstName + " " + user.SurName);
                    }else {
                        Intent intent = new Intent(getApplicationContext() ,SigninActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getBaseContext(), "You need create your profile .", Toast.LENGTH_LONG).show();
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

    private  void getGroup(){
        rootDataBase.child("groups").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Group group = userSnapshot.getValue(Group.class);
                    if(group!=null) {
                        listGroup.add(group);

                    }

                }

                if(listGroup.size()!=0) {
                    createListGroup(listGroup);
                    fab.setVisibility(LinearLayout.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void createListGroup(ArrayList<Group> listGroup){

        listGroups = (RecyclerView)findViewById(R.id.listGroup);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);


        //put gridManager to recyclerview
        listGroups.setLayoutManager(gridLayoutManager);
        listGroups.setHasFixedSize(true);

        //create new adapter
        groupListAdapter = new GroupListAdapter(listGroup);

        //put adapter to recyclerview
        listGroups.setAdapter(groupListAdapter);


    }

    @Override
    public void onPause() {
        super.onPause();
        Common.aplicationVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.aplicationVisible = true;
    }
}
