package com.appoftatar.workgroupcalendar.ui.schedule;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.RedactorScheduleActivity;
import com.appoftatar.workgroupcalendar.AdditionalScheduleActivity;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.adapters.ShiftAdapter;
import com.appoftatar.workgroupcalendar.models.Schedule;
import com.appoftatar.workgroupcalendar.models.Screenshot;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class WorkScheduleFragment extends Fragment {

    private LinearLayout main;
    private LinearLayout rlButtons;
    private TextView tvDateTo;
    private TextView tvDateFrom;
    private TextView tvNameGroup;
    private TextView tvTitle;
    private RecyclerView rv_shift1;
    private RecyclerView rv_shift2;
    private RecyclerView rv_shift3;
    private Schedule newSchedale;
    private Button btnAdditionalSchedule;
    private Button btnCreateSchedule;
    private Button btnShare;
    private File fileScreenshot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_work_schedule, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        rv_shift1 = (RecyclerView)root.findViewById(R.id.rv_shift1);
        rv_shift2 = (RecyclerView)root.findViewById(R.id.rv_shift2);
        rv_shift3 = (RecyclerView)root.findViewById(R.id.rv_shift3);

        main = (LinearLayout) root.findViewById(R.id.main);
        rlButtons = (LinearLayout) root.findViewById(R.id.rlButtons);

        tvDateTo = (TextView) root.findViewById(R.id.tvDateTo);
        tvDateFrom = (TextView) root.findViewById(R.id.tvDateFrom);
        tvNameGroup = (TextView) root.findViewById(R.id.tvNameGroup);
        tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        btnAdditionalSchedule = (Button)root.findViewById(R.id.btnAdditionalSchedule);
        btnShare = (Button)root.findViewById(R.id.btnShare);
        btnCreateSchedule = (Button)root.findViewById(R.id.btnCreateSchedule);

        btnCreateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , RedactorScheduleActivity.class);
                startActivity(intent);
            }
        });
        if(!Common.manager){
            btnCreateSchedule.setVisibility(LinearLayout.INVISIBLE);
            btnCreateSchedule.setVisibility(LinearLayout.INVISIBLE);
        }


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.manager)
                    tvTitle.setText(Common.currentGroup);
                else tvTitle.setText(Common.currentUser.IdWorkGroup);
                btnCreateSchedule.setVisibility(LinearLayout.INVISIBLE);
                btnAdditionalSchedule.setVisibility(LinearLayout.INVISIBLE);

                Bitmap mBitmap = Screenshot.takeScreenshotOfRootView(main);

                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));

                btnCreateSchedule.setVisibility(LinearLayout.VISIBLE);
                btnAdditionalSchedule.setVisibility(LinearLayout.VISIBLE);
            }
        });


        btnAdditionalSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdditionalScheduleActivity.class);
                startActivity(intent);
            }
        });

        getSchedale();
        return root;
    }

    public static WorkScheduleFragment getInstance(){
        Bundle args = new Bundle();
        WorkScheduleFragment fragment = new WorkScheduleFragment();
        fragment.setArguments(args);

        return fragment;
    }
    private void initLists(){



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());


        //put gridManager to recyclerview
        rv_shift1.setLayoutManager(linearLayoutManager);
        rv_shift2.setLayoutManager(linearLayoutManager2);
        rv_shift3.setLayoutManager(linearLayoutManager3);


        rv_shift1.setHasFixedSize(true);
        rv_shift2.setHasFixedSize(true);
        rv_shift3.setHasFixedSize(true);

        //create new adapters
        ShiftAdapter shift1Adapter = new ShiftAdapter(hashMapToArrayList(newSchedale.getShift1()),false,null);
        ShiftAdapter shift2Adapter = new ShiftAdapter(hashMapToArrayList(newSchedale.getShift2()),false,null);
        ShiftAdapter shift3Adapter = new ShiftAdapter(hashMapToArrayList(newSchedale.getShift3()),false,null);



        //put adapter to recyclerview
        rv_shift1.setAdapter(shift1Adapter);
        rv_shift2.setAdapter(shift2Adapter);
        rv_shift3.setAdapter(shift3Adapter);

    }

    private ArrayList<User> hashMapToArrayList(HashMap<String,User> hashMap){
        ArrayList<User> arList = new ArrayList<>();

        for(Integer i = 0; i < hashMap.size(); i++){
            arList.add(hashMap.get(i.toString()));
        }

        return arList;
    }


    private void getSchedale(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        String idManager = Common.currentUser.ID;
        String group = Common.currentGroup;
        if(!Common.manager){
            idManager = Common.currentUser.IdManager;
            group = Common.currentUser.IdWorkGroup;
        }
        root.child("schedules").child(idManager).child(group).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot scheduleSnapshot: dataSnapshot.getChildren()) {
                    if(scheduleSnapshot.getKey().equals("newSchedule")) {
                        Schedule schedule = new Schedule();
                        schedule.setDateFrom(scheduleSnapshot.child("dateFrom").getValue(Date.class));
                        schedule.setDateTo(scheduleSnapshot.child("dateTo").getValue(Date.class));


                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift1").getChildren())
                            schedule.setEmployeeToShift(1,itemSnapshot.getValue(User.class));

                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift2").getChildren())
                            schedule.setEmployeeToShift(2,itemSnapshot.getValue(User.class));

                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift3").getChildren())
                            schedule.setEmployeeToShift(3,itemSnapshot.getValue(User.class));


                        newSchedale = schedule;
                        Date date = new Date();
                        SimpleDateFormat fromDateFormat =  new SimpleDateFormat("dd/MM");
                        tvDateTo.setText(fromDateFormat.format(newSchedale.getDateTo())+"/"+Integer.toString(date.getYear()+1900));
                        SimpleDateFormat fromDateFormat2 =  new SimpleDateFormat("dd/MM");
                        tvDateFrom.setText(fromDateFormat2.format(newSchedale.getDateFrom()));
                        initLists();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }


}
