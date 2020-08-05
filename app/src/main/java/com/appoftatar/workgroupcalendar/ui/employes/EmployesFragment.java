package com.appoftatar.workgroupcalendar.ui.employes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.EmployeeListAdapter;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.SignupActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployesFragment extends Fragment {

    FloatingActionButton fab;
    private RecyclerView listEmployes;
    private EmployeeListAdapter groupListAdapter;
    private ArrayList<User> lEmployes;
    private DatabaseReference rootDataBase;
    private TextView tvNameGroup;
    private LinearLayout ll;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_employes, container, false);
        tvNameGroup = root.findViewById(R.id.tvNameGroupEmployes);

        tvNameGroup.setText(Common.currentGroup);
        listEmployes = root.findViewById(R.id.rsListEmployes);
        getListEmployeesFromDataBase();
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , SignupActivity.class);
                intent.putExtra("NAME_GROUP", Common.currentGroup);
                intent.putExtra("ID_MANAGER", Common.currentUser.ID);
                startActivity(intent);

            }
        });

        return root;
    }

    public static EmployesFragment getInstance(){
        Bundle args = new Bundle();
        EmployesFragment fragment = new EmployesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void createListEmployes(ArrayList<User> lEmployes){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);

        //put gridManager to recyclerview
        listEmployes.setLayoutManager(gridLayoutManager);
        listEmployes.setHasFixedSize(true);

        //create new adapter
        groupListAdapter = new EmployeeListAdapter(lEmployes);

        //put adapter to recyclerview
        listEmployes.setAdapter(groupListAdapter);

    }

    private void getListEmployeesFromDataBase(){
        lEmployes = new ArrayList<>();
        rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("users").orderByChild("IdWorkGroup").equalTo(Common.currentGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.IdManager.equals(Common.currentUser.ID))
                    lEmployes.add(user);

                }
                if(lEmployes.size()!=0) {
                    createListEmployes(lEmployes);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }


}
