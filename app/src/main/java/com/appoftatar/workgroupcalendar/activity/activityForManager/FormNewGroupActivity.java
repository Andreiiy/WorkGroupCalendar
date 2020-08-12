package com.appoftatar.workgroupcalendar.activity.activityForManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.CreateGroupActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormNewGroupActivity extends AppCompatActivity {

    private  EditText nameGroupInput;
    private Button btnCreateGroup;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private boolean checkValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_group);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        nameGroupInput = (EditText)findViewById(R.id.input_nameGroup);

        btnCreateGroup = (Button)findViewById(R.id.createGroupButton);
        ImageView ivTitle = (ImageView)findViewById(R.id.ivTitle);
        ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,200,200));
        Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,150,30);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btnCreateGroup.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        else {
            btnCreateGroup.setBackground(new BitmapDrawable(getResources(),bitmap));
        }
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewGroup(nameGroupInput.getText().toString());
            }
        });

    }

    private void writeNewGroup( String nameGroup){
        if(validation(nameGroup)) {

            String userID = mAuth.getCurrentUser().getUid();
            Group group = new Group(nameGroup,userID);

            mDatabase.child("groups").child(userID).push().setValue(group)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getBaseContext(), CreateGroupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        }
    }

private boolean validation(String nameGroup){
        checkValid=true;
    if(nameGroupInput.getText().toString().isEmpty()){
        nameGroupInput.setError("Enter name of group");
        checkValid=false;
    }else {
for(Group group : Common.listGroup){
    if(group.nameGroup.equals(nameGroup)){
        checkValid=false;
        nameGroupInput.setError("This name already exists");
    }
}
    }
    return checkValid;
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

