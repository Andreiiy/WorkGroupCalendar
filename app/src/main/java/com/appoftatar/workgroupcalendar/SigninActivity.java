package com.appoftatar.workgroupcalendar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.appoftatar.workgroupcalendar.Common.Common;


import com.appoftatar.workgroupcalendar.di.components.DaggerEntranceComponent;
import com.appoftatar.workgroupcalendar.di.components.EntranceComponent;
import com.appoftatar.workgroupcalendar.di.modules.SigninViewModule;
import com.appoftatar.workgroupcalendar.di.modules.SignupViewModule;
import com.appoftatar.workgroupcalendar.presenters.SigninPresenter;
import com.appoftatar.workgroupcalendar.views.SigninView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

public class SigninActivity extends AppCompatActivity implements SigninView {
       //region //============= FIELDS ===================//
    private boolean check = false;

    private static final int REQUEST_SIGNUP = 0;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _link_reset;
    public SharedPreferences pref;
    public ProgressDialog progressDialog;
    CoordinatorLayout cordLayoutSignin;

    private EntranceComponent component;
    @Inject
    SigninPresenter presenter;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //region //========= INITIALISATION VIEW ELEMENTS =============//
       initViews();
       //endregion


        component = DaggerEntranceComponent.builder().signinViewModule(new SigninViewModule(this))
                .signupViewModule(new SignupViewModule(null)).build();
        component.inject(this);



        //region =====================  PREFERENSES =======================//
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String saveEmail = pref.getString("key_email", null);
        String savePassword = pref.getString("key_pass", null);
        if(saveEmail != null && savePassword != null) {
            _emailText.setText(saveEmail); // getting String
            _passwordText.setText(savePassword); // getting Integer
        }
        //endregion


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                @SuppressLint("CommitPrefEdits")
                SharedPreferences.Editor  editor = pref.edit();
                editor.putString("key_email", _emailText.getText().toString()); // Storing string
                editor.putString("key_pass", _passwordText.getText().toString());
                editor.apply();

                String email = _emailText.getText().toString().trim();
                String password = _passwordText.getText().toString().trim();
                presenter.signin(email,password);

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignupActivity.class);
                intent.putExtra("NAME_GROUP","0");
                startActivity(intent);
            }
        });

        _link_reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        if(!Common.manager)
            _signupLink.setHeight(0);
    }

private void initViews(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ImageView ivTitle = (ImageView) findViewById(R.id.ivTitle);
    ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,150,150));

    cordLayoutSignin = (CoordinatorLayout)findViewById(R.id.cordLayoutSignin);

    _emailText = (EditText)findViewById(R.id.input_email);
    _passwordText = (EditText)findViewById(R.id.input_password);
    _loginButton = (Button) findViewById(R.id._loginButton);
    Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,150,30);
    if(android.os.Build.VERSION.SDK_INT < 16) {
        _loginButton.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
    }
    else {
        _loginButton.setBackground(new BitmapDrawable(getResources(),bitmap));
    }
    _signupLink = (TextView)findViewById(R.id.link_signup);
    _link_reset = (TextView)findViewById(R.id.link_reset);

    progressDialog = new ProgressDialog(SigninActivity.this,
            R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage(getResources().getText(R.string.dialog_auth)+"...");
}

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        Intent intent = new Intent(getBaseContext(), StatusSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(SigninActivity.this, getResources().getText(R.string.tost_autsucc), Toast.LENGTH_LONG).show();
        if(Common.manager) {
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, EmployeeHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void emailTextError(Boolean checked) {
        if(checked)
            _emailText.setError(getResources().getText(R.string.input_email));
        else
            _emailText.setError(null);
    }

    @Override
    public void passwordTextError(Boolean checked) {
        if(checked)
            _passwordText.setError(getResources().getText(R.string.input_pass));
        else
            _passwordText.setError(null);
    }

    @Override
    public void onLoginFailed() {
        Snackbar snackbar = Snackbar
                .make(cordLayoutSignin, getResources().getText(R.string.snack_authfailed), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorRedorange);
        snackbar.setTextColor(Color.YELLOW);
        snackbar.show();
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
    public void showProgressBar() {
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}
