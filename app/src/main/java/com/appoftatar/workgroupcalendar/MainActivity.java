package com.appoftatar.workgroupcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import java.util.Timer;
import java.util.TimerTask;

import static com.appoftatar.workgroupcalendar.R.*;

public class MainActivity extends AppCompatActivity {

    private Animation topAnim;
    ImageView titleImage;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, anim.top_animation);
        titleImage = (ImageView)findViewById(id.titleImage);
        titleImage.setAnimation(topAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, StatusSelectionActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
