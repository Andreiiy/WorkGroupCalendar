package com.appoftatar.workgroupcalendar.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder>{
    private ArrayList<MsgOnBoard> listMessages;

    public BoardAdapter(ArrayList<MsgOnBoard> messages) {

        this.listMessages = messages;
    }

    @NonNull
    @Override
    public BoardAdapter.BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_msg;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        BoardAdapter.BoardViewHolder viewHolder = new BoardAdapter.BoardViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.BoardViewHolder holder, int position) {
        holder.bind(listMessages.get(position),position);
    }


    @Override
    public int getItemCount() {
        return listMessages.size();
    }



    class  BoardViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMessage;
        TextView tvDate;
        TextView tvName;
        TextView tvMsgInboard;
        private MsgOnBoard message;
        ImageView ivKnpk;
        private int position;
        public BoardViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMsgInboard = itemView.findViewById(R.id.tvMsgInboard);
            tvName = itemView.findViewById(R.id.tvName);
            ivKnpk = itemView.findViewById(R.id.knpk);

itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, v.getId(), 0, "Text is Copy");
        ViewGroup vg = (ViewGroup) v;
        View childLL = vg.getChildAt(0);
        ViewGroup vgLL = (ViewGroup) childLL;
        View child = vgLL.getChildAt(2);
        tvMsgInboard = (TextView) child;
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(tvMsgInboard.getText());
    }
});
        }

        void bind(MsgOnBoard msg,int position) {
            if(position>0)
                ivKnpk.setVisibility(LinearLayout.GONE);
            this.message = msg;

            tvDate.setText(message.date);
            tvMsgInboard.setText(message.text);
            tvName.setText(message.nameAuthor);
            if(message.statusManager.equals("true"))
                layoutMessage.setBackgroundResource(R.color.colorRedLight);
                //layoutMessage.setBackgroundColor(Color.RED);

        }

    }

}
