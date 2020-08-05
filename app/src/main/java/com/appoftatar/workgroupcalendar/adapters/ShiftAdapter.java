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

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder> {

    private ArrayList<User> listEmployes;
    private OnEmployeeClickListener onEmployeeClickListener;
    private boolean redactor;

    public ShiftAdapter(ArrayList<User> listEmployes,boolean redactor, OnEmployeeClickListener onEmployeeClickListener) {
        this.listEmployes = listEmployes;
        this.onEmployeeClickListener = onEmployeeClickListener;
        this.redactor = redactor;
    }





    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = 0;
       if(redactor)
           layoutIdForListItem = R.layout.item_list_shift;
       else
           layoutIdForListItem = R.layout.item_list_employee_for_screen;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        ShiftAdapter.ShiftViewHolder viewHolder = new ShiftAdapter.ShiftViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
     holder.bind(listEmployes.get(position));
    }

    @Override
    public int getItemCount() {
        return listEmployes.size();
    }

    public class ShiftViewHolder extends RecyclerView.ViewHolder{

        TextView tvFullName;
        User employee;
        public ShiftViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvFullName = itemView.findViewById(R.id.tvnameEmployee);
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(onEmployeeClickListener !=null)
                  onEmployeeClickListener.onEmployeeClick(employee);
              }
          });
        }

        public void bind(User employee){
            this.employee = employee;
            tvFullName.setText(employee.FirstName+"."+employee.SurName.charAt(0) );
        }
    }

     public interface OnEmployeeClickListener {
        void onEmployeeClick(User user);
    }
}
