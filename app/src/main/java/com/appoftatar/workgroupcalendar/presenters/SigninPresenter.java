package com.appoftatar.workgroupcalendar.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.R;
//import com.appoftatar.workgroupcalendar.di.components.DaggerFireBaseComponent;
import com.appoftatar.workgroupcalendar.di.components.DaggerFireBaseComponent;
import com.appoftatar.workgroupcalendar.di.components.FireBaseComponent;
import com.appoftatar.workgroupcalendar.views.SigninView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

public class SigninPresenter {

    private FirebaseAuth mAuth;
    private DatabaseReference rootDatabase;
    private FireBaseComponent fireBaseComponent;

    private SigninView view;

    @Inject
    public SigninPresenter(SigninView view) {
        this.view = view;
        fireBaseComponent = DaggerFireBaseComponent.builder().build();

        mAuth = fireBaseComponent.getFirebaseAuth();

    }

    public void signin(String email,String password){

        if (!validate(email,password)) {
            view.onLoginFailed();
            return;
        }


        view.showProgressBar();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    view.hideProgressBar();
                    view.onLoginSuccess();

                } else {
                    Log.d("myTag", "e.toString()");
                    view.hideProgressBar();
                    view.onLoginFailed();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("myTag", e.toString());
            }
        });

    }
    public boolean validate(String email,String password) {
        boolean valid = true;

        if (email.isEmpty()) {
            view.emailTextError(true);
            valid = false;
        } else {
            view.emailTextError(false);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            view.passwordTextError(true);
            valid = false;
        } else {
            view.passwordTextError(false);
        }

        return valid;
    }
}
