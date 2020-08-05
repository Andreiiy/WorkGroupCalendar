package com.appoftatar.workgroupcalendar.ui.home;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.adapters.TabsPagerEmployeeFragmentAdapter;
import com.appoftatar.workgroupcalendar.calendar.WorkDayWithRemainder;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.gms.common.internal.Constants;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collections;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    View root;
    ViewPager viewPager;
    private FirebaseAuth mAuth;
    private boolean actionActivity;
    private NotificationManager notificationManager;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootDataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_home_employee, container, false);
        initTabs();
        createNotificationAndChannel(getContext());
        getCurrentUser();

        return root;
    }

    private void initTabs() {
        viewPager = root.findViewById(R.id.view_pager);
        TabsPagerEmployeeFragmentAdapter adapter = new TabsPagerEmployeeFragmentAdapter(getChildFragmentManager(),getContext());
        viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tabLay);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.employees);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_board);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_calendar);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_schedule);
        tabLayout.getTabAt(4).setIcon(R.drawable.icon_workcalendar);
    }


    private void eventChangeValueInBoard(){
        DatabaseReference refRequests = FirebaseDatabase.getInstance().getReference();
        refRequests.child("boards").child(Common.currentUser.IdManager).orderByChild("nameGroup").equalTo(Common.currentUser.IdWorkGroup).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    MsgOnBoard message = userSnapshot.getValue(MsgOnBoard.class);
                    if (message != null) {
                        if (! Common.aplicationVisible) {
                            showNotifikation(getString(R.string.not_msg) + " " + getString(R.string.not_board),null);
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



    private void getRemaindersToday(){

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        root.child("remainders").child(Integer.toString(calendar.getTime().getYear()+1900))
                .child(Integer.toString(calendar.getTime().getMonth())).child(Integer.toString(calendar.getTime().getDate())).child(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    WorkDayWithRemainder workDayWithRemainder = new WorkDayWithRemainder();
                    workDayWithRemainder = dateSnapshot.getValue(WorkDayWithRemainder.class);


                    if (workDayWithRemainder != null &&
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<=workDayWithRemainder.getHours()
                            && Calendar.getInstance().get(Calendar.MINUTE)<=workDayWithRemainder.getMinutes())
                        startAlarm(workDayWithRemainder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }

        });

    }

    private void startAlarm(final WorkDayWithRemainder workDayWithRemainder) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,workDayWithRemainder.getDate());
        calendar.set(Calendar.HOUR_OF_DAY,workDayWithRemainder.getHours());
        calendar.set(Calendar.MINUTE,workDayWithRemainder.getMinutes());

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent )
            {

                showNotifikation(workDayWithRemainder.getEvent(),"Alarm");
                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
            }
        };

        getContext().registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( getContext(), 0, new Intent("com.blah.blah.somemessage"), 0 );
        AlarmManager manager = (AlarmManager)(getContext().getSystemService( Context.ALARM_SERVICE ));
        manager.set( AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pintent );

    }

    private void showNotifikation(String message,String alarm ){

        long[] pattern;
        String contentTitle="";
        if(alarm!=null) {
            pattern = new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
            contentTitle = "Alarm";
        }
        else {
            contentTitle = "New message";
            pattern = new long[]{1000, 1000};

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"Work");
        Intent intent = new Intent(getContext(), HomeFragment.class);
        PendingIntent pendingIntent =  PendingIntent.getActivity(getContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_aplication)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher_background))
                .setTicker("New message")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle(contentTitle)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_aplication))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_HIGH);


        notificationManager.notify(1,builder.build());
        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern,-1);

    }



    private void createNotificationAndChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Work Massage";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channal = new NotificationChannel("Work", name, NotificationManager.IMPORTANCE_HIGH);
            channal.enableVibration(true);
            channal.setVibrationPattern(new long[] { 1000, 1000 , 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 });
            channal.enableLights(true);
            channal.setShowBadge(true);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channal);
        }


    }
    private void getCurrentUser(){


        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getInstance().getCurrentUser();
        rootDataBase = FirebaseDatabase.getInstance().getReference("users");

        DatabaseReference userRef = rootDataBase.child(mAuth.getUid() + "/");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(!Common.manager && user.Manager.equals("false")) {
                        Common.currentUser = user;

                        eventChangeValueInBoard();
                        getRemaindersToday();
                    }
                }

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
