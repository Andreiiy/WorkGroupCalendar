package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.InformationEnployeeActivity;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.R;

import java.util.ArrayList;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

    private ArrayList<User> listEmployes;
    public EmployeeListAdapter(ArrayList<User> listEmployes) {
        this.listEmployes = listEmployes;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_employes;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        EmployeeListAdapter.EmployeeViewHolder viewHolder = new EmployeeListAdapter.EmployeeViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        holder.bind(listEmployes.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.listEmployes.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        User user;
        public EmployeeViewHolder(@NonNull View itemView,final Context context) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvnameEmployee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InformationEnployeeActivity.class);
                    intent.putExtra("USER_ID",user.ID);
                    intent.putExtra("FIRST_NAME",user.FirstName);
                    intent.putExtra("SUR_NAME",user.SurName);
                    intent.putExtra("TELEFON",user.Telefon);
                    intent.putExtra("EMAIL",user.Email);
                    intent.putExtra("PASSWORD",user.Password);
                    context.startActivity(intent);
                }
            });
        }

        public void  bind(User user, int position){
            this.user = user;
            tvUserName.setText(user.FirstName +" "+user.SurName);
        }
    }
}
