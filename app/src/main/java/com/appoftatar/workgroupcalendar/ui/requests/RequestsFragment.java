package com.appoftatar.workgroupcalendar.ui.requests;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

import com.appoftatar.workgroupcalendar.adapters.RequestsAdapter;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class RequestsFragment extends Fragment {
    private RecyclerView listRequests;
    private RequestsAdapter requestsListAdapter;
    private ArrayList<HolidayRequest> lRequests;
    private DatabaseReference rootDataBase;
    private boolean actionActivity = true;
    private NotificationManager notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        listRequests = root.findViewById(R.id.rvRequests);
        getListEmployeesFromDataBase();

        return root;
    }

    private void getListEmployeesFromDataBase() {

        rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("requests").child(Common.currentUser.ID).child(Common.currentGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lRequests = new ArrayList<>();
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                    if(requestSnapshot.child("answerRequest").getValue().toString().equals("0")) {
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

                        lRequests.add(request);
                    }
                }
                if(lRequests.size()!=0) {
                    createListEmployes(lRequests);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }
    private void createListEmployes(ArrayList<HolidayRequest> lRequests) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);

        //put gridManager to recyclerview
        listRequests.setLayoutManager(gridLayoutManager);
        listRequests.setHasFixedSize(true);

        //create new adapter
        requestsListAdapter = new RequestsAdapter(lRequests);

        //put adapter to recyclerview
        listRequests.setAdapter(requestsListAdapter);
    }

    public static RequestsFragment getInstance(){
        Bundle args = new Bundle();
        RequestsFragment fragment = new RequestsFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
