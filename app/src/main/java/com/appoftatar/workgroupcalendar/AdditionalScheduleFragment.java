package com.appoftatar.workgroupcalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.adapters.ShiftAdapter;
import com.appoftatar.workgroupcalendar.models.Schedule;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.Screenshot;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AdditionalScheduleFragment extends Fragment {


    private FloatingActionButton fabEdit;
    private TextView tvDate;
    private TextView tv_weekday;
    private RecyclerView rv_shift1;
    private RecyclerView rv_shift2;
    private RecyclerView rv_shift3;
    private Schedule newSchedale;
    private Integer  currentDay;
    private boolean forScreen = false;
    private Button btnShare;
    private File fileScreenshot;
    private View root;
    private TextView tvGroup;

    public AdditionalScheduleFragment(Integer  currentDay) {
        this.currentDay = currentDay;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_additional_schedule, container, false);
        rv_shift1 = (RecyclerView)root.findViewById(R.id.rv_shift1);
        rv_shift2 = (RecyclerView)root.findViewById(R.id.rv_shift2);
        rv_shift3 = (RecyclerView)root.findViewById(R.id.rv_shift3);

        btnShare = (Button)root.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(Common.manager)
                    tvGroup.setText(getResources().getText(R.string.tv_group)+" "+Common.currentGroup);
                else
                    tvGroup.setText(getResources().getText(R.string.tv_group)+" "+Common.currentUser.IdWorkGroup);

                Bitmap mBitmap = Screenshot.takeScreenshotOfRootView(root);


                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));

                tvGroup.setText(getResources().getText(R.string.tv_work_schedule));


            }
        });


        tvDate = (TextView) root.findViewById(R.id.tvDate);
        tv_weekday = (TextView) root.findViewById(R.id.tv_weekday);

        tvGroup=(TextView) root.findViewById(R.id.tvGroup);
        getSchedale();

        return root;
    }


    private void shareIt() {

        Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(fileScreenshot);
        sharingIntent.setDataAndType(uri,"image/*");
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.startActivity(sharingIntent);
    }


    public static AdditionalScheduleFragment getInstance(Integer  currentDay){
        Bundle args = new Bundle();
        AdditionalScheduleFragment fragment = new AdditionalScheduleFragment(currentDay);
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

                for (DataSnapshot addScheduleSnapshot: dataSnapshot.getChildren()) {
                    if(addScheduleSnapshot.getKey().equals("additionalSchedule"))
                        for (DataSnapshot scheduleSnapshot: addScheduleSnapshot.getChildren()) {
                            if (scheduleSnapshot.getKey().equals(currentDay.toString())) {
                                Schedule schedule = new Schedule();
                                schedule.setDateFrom(scheduleSnapshot.child("dateFrom").getValue(Date.class));


                                for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift1").getChildren())
                                    schedule.setEmployeeToShift(1, itemSnapshot.getValue(User.class));

                                for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift2").getChildren())
                                    schedule.setEmployeeToShift(2, itemSnapshot.getValue(User.class));

                                for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift3").getChildren())
                                    schedule.setEmployeeToShift(3, itemSnapshot.getValue(User.class));


                                newSchedale = schedule;
                                SimpleDateFormat fromDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                tvDate.setText(fromDateFormat.format(newSchedale.getDateFrom()));
                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week abbreviated
                                tv_weekday.setText(simpleDateformat.format(newSchedale.getDateFrom()));

                                initLists();
                            }
                        }
                }

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
