package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.AnsversAdapter;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class AnsverToRequestActivity extends AppCompatActivity {

    RecyclerView rv_ansvers;
    private DatabaseReference rootDataBase;
    private ImageView ivTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ansver_to_request);
        ivTitle = (ImageView)findViewById(R.id.ivTitle);
        ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,150,150));

        rv_ansvers = (RecyclerView)findViewById(R.id.rv_ansvers);
        getRequestsFromDataBase();
    }


    private void initRecyclerView(ArrayList<HolidayRequest> lRequests){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //put gridManager to recyclerview
        rv_ansvers.setLayoutManager(linearLayoutManager);
        rv_ansvers.setHasFixedSize(true);

        //create new adapter
        AnsversAdapter requestsListAdapter = new AnsversAdapter(lRequests);

        //put adapter to recyclerview
        rv_ansvers.setAdapter(requestsListAdapter);
    }

    private void getRequestsFromDataBase(){

            rootDataBase = FirebaseDatabase.getInstance().getReference();

            rootDataBase.child("requests").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                    .orderByChild("idEmployee").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     ArrayList<HolidayRequest> listRequests = new ArrayList<>();
                     listRequests.clear();
                    for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                        if(     requestSnapshot.child("viewEmployee").getValue().toString().equals("false"))
                        {
                            if (requestSnapshot.child("answerRequest").getValue().toString().equals("true") ||
                                    requestSnapshot.child("answerRequest").getValue().toString().equals("false")) {

                                HolidayRequest request = new HolidayRequest();
                                request.setId(requestSnapshot.child("id").getValue().toString());
                                request.setIdEmployee(requestSnapshot.child("idEmployee").getValue().toString());
                                request.setNameEmployee(requestSnapshot.child("nameEmployee").getValue().toString());
                                request.setReasonRequest(requestSnapshot.child("reasonRequest").getValue().toString());
                                request.setViewEmployee(requestSnapshot.child("viewEmployee").getValue().toString());
                                request.setAnswerRequest(requestSnapshot.child("answerRequest").getValue().toString());
                                request.setRejectionReason(requestSnapshot.child("rejectionReason").getValue().toString());
                                request.setDate(requestSnapshot.child("date").getValue().toString());


                                for (DataSnapshot itemSnapshot : requestSnapshot.child("dateList").getChildren())
                                    request.setDateToList(itemSnapshot.getValue(Date.class));

                                listRequests.add(request);
                            }
                        }
                    }
                    initRecyclerView(listRequests);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Data base", "Failed to read value.", databaseError.toException());
                }
            });
        }

    private void changeStatusRequests(){


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(),EmployeeHomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rootDataBase = FirebaseDatabase.getInstance().getReference();

        rootDataBase.child("requests").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                .orderByChild("idEmployee").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                    if(     requestSnapshot.child("viewEmployee").getValue().toString().equals("false") &&
                            requestSnapshot.child("answerRequest").getValue().toString().equals("true") ||
                            requestSnapshot.child("answerRequest").getValue().toString().equals("false") ) {

                        requestSnapshot.child("viewEmployee").getRef().setValue("true");
                    }

                }
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

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

