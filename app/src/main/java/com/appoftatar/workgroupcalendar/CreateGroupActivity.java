package com.appoftatar.workgroupcalendar;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.GroupListAdapter;
import com.appoftatar.workgroupcalendar.di.components.DaggerManagerApiComponent;
import com.appoftatar.workgroupcalendar.di.components.ManagerApiComponent;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.GroupsViewModule;
import com.appoftatar.workgroupcalendar.models.Group;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.presenters.GroupsPresenter;
import com.appoftatar.workgroupcalendar.views.GroupsView;
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

import javax.inject.Inject;

public class CreateGroupActivity extends AppCompatActivity implements GroupsView {
    //region ============= FIELDS ===================//
    private RecyclerView listGroups;
    private TextView tvUserName;
    private GroupListAdapter groupListAdapter;
    private FloatingActionButton fab;
    public ProgressDialog progressDialog;
    ManagerApiComponent component;
    @Inject
    GroupsPresenter presenter;

   //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //============= INITIALISATION VIEW ELEMENTS ===================//
        initViews();

        component = DaggerManagerApiComponent.builder().groupsViewModule(new GroupsViewModule(this)).build();
        component.inject(this);
        //get all group
        presenter.showGroups();

   }

   private void initViews(){
       tvUserName = (TextView)findViewById(R.id.nameManager);
       listGroups = (RecyclerView)findViewById(R.id.listGroup);
       fab = findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getApplicationContext() ,FormNewGroupActivity.class);
               startActivity(intent);

           }
       });

       progressDialog = new ProgressDialog(CreateGroupActivity.this,
               R.style.AppTheme_Dark_Dialog);
       progressDialog.setIndeterminate(true);
       progressDialog.setMessage("Get Data...");
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
    public void userLogout() {
        Intent intent = new Intent(getApplicationContext() , SigninActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(getBaseContext(), "You need create your profile .", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUserName(String name) {
        tvUserName.setText(name);
    }

    @Override
    public void showGroups(ArrayList<Group> listGroup){

        //put gridManager to recyclerview
        listGroups.setLayoutManager(new GridLayoutManager(this,1));
        listGroups.setHasFixedSize(true);

        //create new adapter
        groupListAdapter = new GroupListAdapter(listGroup);

        //put adapter to recyclerview
        listGroups.setAdapter(groupListAdapter);

        fab.setVisibility(LinearLayout.INVISIBLE);
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
