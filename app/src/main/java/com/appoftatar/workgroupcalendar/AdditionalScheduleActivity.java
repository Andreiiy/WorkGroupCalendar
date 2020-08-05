package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.TabsPagerAdditionalScheduleAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdditionalScheduleActivity extends AppCompatActivity {
    TabLayout tabLayout;
    View root;
    ViewPager viewPager;
    Button btnNewSchedule;
    TextView tvGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_schedule);

        btnNewSchedule=(Button)findViewById(R.id.btnNewSchedule);

        btnNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ConstructorAdditionalScheduleActivity.class);
                startActivity(intent);
            }
        });
        if(!Common.manager)
            btnNewSchedule.setVisibility(LinearLayout.INVISIBLE);

        getAmountSchedales();
    }

    private void initTabs(Integer amountTabs) {
        viewPager = findViewById(R.id.view_pagerAdditionalSchedule);
        TabsPagerAdditionalScheduleAdapter adapter = new TabsPagerAdditionalScheduleAdapter(getSupportFragmentManager(),amountTabs);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLay);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void getAmountSchedales(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        String idManager = Common.currentUser.ID;
        String group = Common.currentGroup;
        if(!Common.manager){
           idManager = Common.currentUser.IdManager;
           group = Common.currentUser.IdWorkGroup;
       }

        root.child("schedules").child(idManager).child(group).child("additionalSchedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer amount = 0;
                for (DataSnapshot scheduleSnapshot: dataSnapshot.getChildren()) {
                    amount++;
                }
                initTabs(amount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

