package com.appoftatar.workgroupcalendar.ui.employes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.CreateGroupActivity;
import com.appoftatar.workgroupcalendar.adapters.EmployeeListAdapter;
import com.appoftatar.workgroupcalendar.di.components.DaggerFireBaseComponent;
import com.appoftatar.workgroupcalendar.di.components.managerComponents.DaggerEmployeesComponent;
import com.appoftatar.workgroupcalendar.di.components.managerComponents.EmployeesComponent;
import com.appoftatar.workgroupcalendar.di.modules.viewsModules.EmployeesModule;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.SignupActivity;
import com.appoftatar.workgroupcalendar.presenters.EmployeesPresenter;
import com.appoftatar.workgroupcalendar.views.EmployesView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class EmployesFragment extends Fragment implements EmployesView {

    FloatingActionButton fab;
    private RecyclerView listEmployes;
    private TextView tvNameGroup;
    public ProgressDialog progressDialog;
    private View root;
    EmployeesComponent component;
    @Inject
    EmployeesPresenter presenter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_employes, container, false);
        initViews();

        component = DaggerEmployeesComponent.builder().employeesModule(new EmployeesModule(this)).build();
        component.inject(this);

        presenter.getListEmployeesFromDataBase();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , SignupActivity.class);
                intent.putExtra("NAME_GROUP", Common.currentGroup);
                intent.putExtra("ID_MANAGER", Common.currentUser.ID);
                startActivity(intent);

            }
        });

        return root;
    }

    public static EmployesFragment getInstance(){
        Bundle args = new Bundle();
        EmployesFragment fragment = new EmployesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void initViews(){
        tvNameGroup = root.findViewById(R.id.tvNameGroupEmployes);

        tvNameGroup.setText(Common.currentGroup);
        listEmployes = root.findViewById(R.id.rsListEmployes);
        fab = root.findViewById(R.id.fab);

        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get Data...");
    }

    @Override
    public void showProgressBar() {
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
       progressDialog.dismiss();
    }

    @Override
    public void showEmployes(ArrayList<User> lEmployes){

        //put gridManager to recyclerview
        listEmployes.setLayoutManager(new GridLayoutManager(getContext(),1));
        listEmployes.setHasFixedSize(true);

        //put adapter to recyclerview
        listEmployes.setAdapter(new EmployeeListAdapter(lEmployes));

    }

}
