package com.appoftatar.workgroupcalendar.presenters;

import android.content.Intent;
import android.icu.text.Collator;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.SigninActivity;
import com.appoftatar.workgroupcalendar.SignupActivity;
import com.appoftatar.workgroupcalendar.di.components.DaggerFireBaseComponent;
import com.appoftatar.workgroupcalendar.di.components.FireBaseComponent;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.views.SignupView;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPresenter {

    FireBaseComponent fireBaseComponent;
    private FirebaseAuth mAuth;
    private SignupView view;

    public SignupPresenter(SignupView view) {
        this.view = view;
        fireBaseComponent = DaggerFireBaseComponent.builder().build();
    }

    public void registrationEmployee(final User user){

            FirebaseAuth newAuth = FirebaseAuth.getInstance();
            newAuth.createUserWithEmailAndPassword(user.Email, user.Password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser userF = mAuth.getInstance().getCurrentUser();
                                if (userF != null) {
                                    user.ID = userF.getUid();
                                    // Add user to database
                                    addUserToDatabase(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    view.registrationFailed();
                                }
                            }
                        }
                    });

    }

    public void registrationManager(final User user) {
        mAuth =  fireBaseComponent.getFirebaseAuth();
                   mAuth.createUserWithEmailAndPassword(user.Email, user.Password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser userF = mAuth.getInstance().getCurrentUser();
                                if (userF != null) {
                                    user.ID = userF.getUid();

                                    // Add user to database
                                    addUserToDatabase(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    view.registrationFailed();
                                }
                            }
                        }
                    });
    }


    public void addUserToDatabase(User user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(user.ID).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        view.registrationSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.registrationFailed();
                    }
                });
    }

    public boolean validate(String firstname,String surname,String telefon,String email, String password) {
        boolean valid = true;

        if (firstname.isEmpty() ) {
            view.showErrorToField("name");
            valid = false;
        } else {
           view.deleteErrorField("name");
        }

        if (surname.isEmpty() ) {
            view.showErrorToField("surname");
            valid = false;
        } else {
            view.deleteErrorField("surname");
        }

        if (telefon.isEmpty() || telefon.length() < 4 || telefon.length() > 15) {
            view.showErrorToField("telefon");
            valid = false;
        } else {
            view.deleteErrorField("telefon");
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showErrorToField("email");
            valid = false;
        } else {
            view.deleteErrorField("email");
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            view.showErrorToField("password");
            valid = false;
        } else {
            view.deleteErrorField("password");
        }

        return valid;
    }
}
