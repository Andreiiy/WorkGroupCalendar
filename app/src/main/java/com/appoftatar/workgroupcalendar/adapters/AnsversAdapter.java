package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AnsversAdapter extends RecyclerView.Adapter<AnsversAdapter.ViewHolderAnsvers> {

private ArrayList<HolidayRequest> requests;
    public AnsversAdapter(ArrayList<HolidayRequest> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolderAnsvers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_ansvers;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        AnsversAdapter.ViewHolderAnsvers viewHolder = new  AnsversAdapter.ViewHolderAnsvers(view, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAnsvers holder, int position) {
           holder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolderAnsvers extends RecyclerView.ViewHolder{

        TextView tvdates;
        TextView tvApproved;
        TextView tvReason;
        TextView tvReasonText;
        TextView titleVacation;
        ImageView ivAnsver;
        Context context;
        private HolidayRequest request;
        public ViewHolderAnsvers(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            tvdates = itemView.findViewById(R.id.tvdates);
            tvApproved = itemView.findViewById(R.id.tvApproved);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvReasonText = itemView.findViewById(R.id.tvReasonText);
            titleVacation = itemView.findViewById(R.id.titleVacation);
            ivAnsver = itemView.findViewById(R.id.ivAnsver);
        }


        public void bind(HolidayRequest request){
            this.request = request;

            DateFormat dateFormatStart = new SimpleDateFormat("dd/MM");
            DateFormat dateFormatFinish = new SimpleDateFormat("dd/MM/yyyy");
            String dateStart = dateFormatStart.format(request.getDateList().get("0"));
            String dateFinish = dateFormatFinish.format(request.getDateList().get(Integer.toString(request.getDateList().size()-1)));
            tvdates.setText(dateStart+" - "+ dateFinish);

            if(request.getAnswerRequest().equals("true")) {
                tvApproved.setText(context.getResources().getString(R.string.titleApproved));
                tvReason.setWidth(0);
                ivAnsver.setBackground(itemView.getResources().getDrawable(R.drawable.approved));
            }
            else {
                tvApproved.setTextColor(Color.RED);
                tvApproved.setText(context.getResources().getString(R.string.titleNotApproved));
                ivAnsver.setBackground(itemView.getResources().getDrawable(R.drawable.notapproved));
                if(request.getRejectionReason()!=null) {
                    tvReasonText.setText(request.getRejectionReason());
                    tvReason.setText(context.getResources().getString(R.string.titleReason));
                }else tvReason.setHeight(0);
            }
        }


    }
}
