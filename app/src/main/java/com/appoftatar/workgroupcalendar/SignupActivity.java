package com.appoftatar.workgroupcalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.activity.activityForManager.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.di.components.DaggerEntranceComponent;
import com.appoftatar.workgroupcalendar.di.components.EntranceComponent;
import com.appoftatar.workgroupcalendar.di.modules.SigninViewModule;
import com.appoftatar.workgroupcalendar.di.modules.SignupViewModule;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.presenters.SignupPresenter;
import com.appoftatar.workgroupcalendar.views.SignupView;

import javax.inject.Inject;

public class SignupActivity extends AppCompatActivity implements SignupView {
    //region ============= FIELDS ===================//
    private EditText _telefonText;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _registrButton;
    private EditText _surNameText;
    private EditText _firstNameText;
    String name_group;
    EntranceComponent component;
    @Inject
    SignupPresenter presenter;
 //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //region ============= INITIALISATION VIEW ELEMENTS ===================//
        initViews();
        //endregion

        component = DaggerEntranceComponent.builder().signupViewModule(new SignupViewModule(this))
                .signinViewModule(new SigninViewModule(null)).build();
        component.inject(this);

        _registrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = _firstNameText.getText().toString();
                String surname = _surNameText.getText().toString();
                String telefon = _telefonText.getText().toString();
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();

                //Validation
                if (presenter.validate(firstname, surname, telefon, email, password)) {
                    Intent intent = getIntent();
                    final String name_group = intent.getStringExtra("NAME_GROUP");
                    String IdManager = intent.getStringExtra("ID_MANAGER");

                    String identManager = "false";
                    if(IdManager==null) {
                        IdManager = "0";
                        identManager = "true";
                    }

                    User myuser = new User("",
                            _firstNameText.getText().toString(),
                            _surNameText.getText().toString(),
                            _emailText.getText().toString(),
                            _telefonText.getText().toString(),
                            name_group,
                            IdManager, "false",
                            _passwordText.getText().toString(),
                            identManager);

                    //Registration
                    if (name_group == null)
                        presenter.registrationManager(myuser);
                    else
                        presenter.registrationEmployee(myuser);
                }
            }
        });



    }

    private void initViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivTitle = (ImageView) findViewById(R.id.ivTitle);
        ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,150,100));

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

    @Override
    public void registrationSuccess() {
        Toast.makeText(SignupActivity.this, "Registration success .", Toast.LENGTH_SHORT).show();
        if(name_group.equals("0")) {
            Intent intent = new Intent(getBaseContext(), SigninActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getBaseContext(), ManagerHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void registrationFailed() {
        Toast.makeText(SignupActivity.this, "Registration failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorToField(String fieldName) {

        switch(fieldName){
            case "name":
                _firstNameText.setError("enter name");
                break;
            case "surname":
                _surNameText.setError("enter surname");
                break;
            case "telefon":
                _telefonText.setError("between 4 and 15 alphanumeric");
                break;
            case "email":
                _emailText.setError("enter a valid email address");
                break;
            case "password":
                _passwordText.setError("between 6 and 10 alphanumeric characters");
                break;
        }
    }

    @Override
    public void deleteErrorField(String fieldName) {
        switch(fieldName){
            case "name":
                _firstNameText.setError(null);
                break;
            case "surname":
                _surNameText.setError(null);
                break;
            case "telefon":
                _telefonText.setError(null);
                break;
            case "email":
                _emailText.setError(null);
                break;
            case "password":
                _passwordText.setError(null);
                break;
        }
    }
}
