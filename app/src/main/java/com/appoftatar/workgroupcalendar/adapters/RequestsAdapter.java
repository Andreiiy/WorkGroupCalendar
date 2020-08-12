package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.activity.activityForManager.Form_request_forManagerActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>{
    private ArrayList<HolidayRequest> listRequests;
    public RequestsAdapter(ArrayList<HolidayRequest> listRequests) {
        this.listRequests = listRequests;
    }

    @NonNull
    @Override
    public RequestsAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_requests;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        RequestsAdapter.RequestViewHolder viewHolder = new RequestsAdapter.RequestViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.RequestViewHolder holder, int position) {
        holder.bind(listRequests.get(position));
    }


    @Override
    public int getItemCount() {
        return listRequests.size();
    }



    class  RequestViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvName;
        TextView tvTotalDays;
        private HolidayRequest request;

        public RequestViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDates);
            tvTotalDays = itemView.findViewById(R.id.tvTotalDays);
            tvName = itemView.findViewById(R.id.tvnameEmployee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Form_request_forManagerActivity.class);
                    intent.putExtra("REQUEST_ID",request.getId());
                    intent.putExtra("EMPLOYEE_NAME",request.getNameEmployee());

                    DateFormat dateFormatStart = new SimpleDateFormat("dd/MM");
                    intent.putExtra("DATE_START",dateFormatStart.format(request.getDateList().get("0")));

                    DateFormat dateFormatFinish = new SimpleDateFormat("dd/MM/yyyy");
                    intent.putExtra("DATE_FINISH",dateFormatFinish.format(request.getDateList().get(Integer.toString(request.getDateList().size()-1))));
                    intent.putExtra("REASON",request.getReasonRequest());

                    intent.putExtra("TOTAL_DAYS",Integer.toString(request.getDateList().size()));
                    intent.putExtra("DATE",request.getDate());

                    context.startActivity(intent);
                }
            });
        }

        void bind(HolidayRequest request) {
            this.request = request;

            DateFormat dateFormatStart = new SimpleDateFormat("dd/MM");
          String firstDay =  dateFormatStart.format(request.getDateList().get("0"));

            DateFormat dateFormatFinish = new SimpleDateFormat("dd/MM/yyyy");
          String lustDay = dateFormatFinish.format(request.getDateList().get(Integer.toString(request.getDateList().size()-1)));

            tvDate.setText(firstDay+" - "+lustDay);

            tvName.setText(request.getNameEmployee());
            tvTotalDays.setText(Integer.toString(request.getDateList().size()));

        }

    }
}
