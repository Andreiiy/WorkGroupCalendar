package com.appoftatar.workgroupcalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.SigninActivity;

public class StatusSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnManager, btnEmployes;
    LinearLayout llEmployes,llManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_selection);

        initViews();
    }

private void initViews(){
    ImageView ivTitle = (ImageView) findViewById(R.id.ivTitle);
    ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,150,150));
    llManager = (LinearLayout)findViewById(R.id.llManager);
    llManager.setOnClickListener(this);
    llEmployes = (LinearLayout)findViewById(R.id.llEmployes);
    llEmployes.setOnClickListener(this);

    btnManager = (Button)findViewById(R.id.btnManager);
    //btnManager.setBackgroundResource(R.drawable.border_second);
    btnManager.setOnClickListener(this);
    btnEmployes = (Button)findViewById(R.id.btnEmployes);
    btnEmployes.setOnClickListener(this);
}
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnManager){
            Intent intent = new Intent(this, SigninActivity.class);
            Common.manager = true;
            startActivity(intent);
        }else if(v.getId() == R.id.btnEmployes){
            Intent intent = new Intent(this,SigninActivity.class);
            Common.manager = false;
            startActivity(intent);
        }else if(v.getId() == R.id.llManager){
            Intent intent = new Intent(this,SigninActivity.class);
            Common.manager = true;
            startActivity(intent);
        }else if(v.getId() == R.id.llEmployes){
            Intent intent = new Intent(this,SigninActivity.class);
            Common.manager = false;
            startActivity(intent);
        }
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
