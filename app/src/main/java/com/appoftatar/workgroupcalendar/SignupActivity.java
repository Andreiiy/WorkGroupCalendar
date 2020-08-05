package com.appoftatar.workgroupcalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    //region ============= FIELDS ===================//
    private FirebaseAuth mAuth;
    private EditText _telefonText;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _registrButton;
    private EditText _surNameText;
    private EditText _firstNameText;
    String name_group;
 //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //region ============= INITIALISATION VIEW ELEMENTS ===================//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivTitle = (ImageView) findViewById(R.id.ivTitle);
        ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,150,100));

        mAuth = FirebaseAuth.getInstance();

        _firstNameText = (EditText) findViewById(R.id.input_firstName);
        _surNameText = (EditText) findViewById(R.id.input_surName);
        _telefonText = (EditText)findViewById(R.id.input_telefon);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);

        _registrButton = (Button)findViewById(R.id._registrButton);
        Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,150,40);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            _registrButton.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        else {
            _registrButton.setBackground(new BitmapDrawable(getResources(),bitmap));
        }
        //endregion
        _registrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailText.getText().toString();
                String pass = _passwordText.getText().toString();
        if(name_group == null)
                registrationManager(email, pass);
        else
            registrationEmployee(email, pass);
            }
        });



    }
    private void registrationEmployee(String email, String password){
        if (validate()) {
            FirebaseAuth newAuth = FirebaseAuth.getInstance();
            newAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registration success .", Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Add user to database
                                    addUserToDatabase(user);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }

    public void registrationManager(String email, String pass) {

        if (validate()) {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registration success .", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Add user to database
                                    addUserToDatabase(user);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        }
    }


    public void addUserToDatabase(FirebaseUser user){
        Intent intent = getIntent();
        final String name_group = intent.getStringExtra("NAME_GROUP");
        String IdManager = intent.getStringExtra("ID_MANAGER");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String firebaseUserID = user.getUid().toString();
        String identManager = "false";
        if(IdManager==null) {
            IdManager = "0";
            identManager = "true";
        }


        User myuser = new User(firebaseUserID,
                _firstNameText.getText().toString(),
                _surNameText.getText().toString(),
                _emailText.getText().toString(),
                _telefonText.getText().toString(),
                name_group,
                IdManager, "false",
                _passwordText.getText().toString(),
                identManager);

        myRef.child("users").child(user.getUid()).setValue(myuser)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(name_group.equals("0")) {
                           boolean b = Common.manager;
                            Intent intent = new Intent(getBaseContext(), SigninActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getBaseContext(), ManagerHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
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

    public boolean validate() {
        boolean valid = true;


        String firstname = _firstNameText.getText().toString();
        String surname = _surNameText.getText().toString();
        String telefon = _telefonText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (firstname.isEmpty() ) {
            _firstNameText.setError("enter name");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (surname.isEmpty() ) {
            _surNameText.setError("enter surname");
            valid = false;
        } else {
            _surNameText.setError(null);
        }

        if (telefon.isEmpty() || telefon.length() < 4 || telefon.length() > 15) {
            _telefonText.setError("between 4 and 15 alphanumeric");
            valid = false;
        } else {
            _telefonText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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
