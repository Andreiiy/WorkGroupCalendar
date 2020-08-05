package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.User;

import java.util.ArrayList;

public class EmployesFromRedactorAdapter extends RecyclerView.Adapter<EmployesFromRedactorAdapter.EmployeeViewHolder> {
    private EmployesFromRedactorAdapter.OnEmployeeClickListener employeeClickListener;
    private ArrayList<User> employes;

    public EmployesFromRedactorAdapter(ArrayList<User> employes,EmployesFromRedactorAdapter.OnEmployeeClickListener employeeClickListener ) {
        this.employes = employes;
        this.employeeClickListener = employeeClickListener;

    }

    @NonNull
    @Override
    public EmployesFromRedactorAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_list_redactor_employee;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        EmployesFromRedactorAdapter.EmployeeViewHolder viewHolder = new EmployesFromRedactorAdapter.EmployeeViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployesFromRedactorAdapter.EmployeeViewHolder holder, int position) {
                holder.bind(employes.get(position));
    }

    @Override
    public int getItemCount() {
        return employes.size();
    }


    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        TextView tvnameEmployee;
        User employee;
        public EmployeeViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvnameEmployee = itemView.findViewById(R.id.tvnameEmployee);
itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        employeeClickListener.onEmployeeClick(employee);
    }
});
        }

        public void bind(User employee){
            this.employee = employee;
            tvnameEmployee.setText(employee.FirstName+" "+employee.SurName );
        }
    }

    public interface OnEmployeeClickListener {
        void onEmployeeClick(User user);
    }



}
