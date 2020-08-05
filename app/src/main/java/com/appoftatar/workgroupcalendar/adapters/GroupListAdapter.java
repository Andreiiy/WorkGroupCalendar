package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.ManagerHomeActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.Group;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>{
    private ArrayList<Group> listGroups;

    public GroupListAdapter(ArrayList<Group> groups) {

        this.listGroups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_groups;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        GroupListAdapter.GroupViewHolder viewHolder = new GroupListAdapter.GroupViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
     holder.bind(listGroups.get(position),position);
    }


    @Override
    public int getItemCount() {
        return listGroups.size();
    }



    class  GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameGroup;

        private Group group;
        private int position;
        public GroupViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            tvNameGroup = itemView.findViewById(R.id.tvnameGroup);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.currentGroup = tvNameGroup.getText().toString();
                    Intent intent = new Intent(context, ManagerHomeActivity.class);
                    context.startActivity(intent);

                }
            });
        }

        void bind(Group grp, int pos) {
            this.group = grp;
            this.position = pos;
            tvNameGroup.setText(group.nameGroup);
        }

    }
}
