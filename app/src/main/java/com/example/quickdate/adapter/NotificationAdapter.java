package com.example.quickdate.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList){
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        holder.tv_name.setText("");
        holder.tv_notification.setText(notification.getNotification());

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(notification.getTimeStamp()));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar).toString();
        holder.tv_timeStamp.setText(pTime);

        try{
            Picasso.get().load("").placeholder(R.drawable.img_doneemoji).into(holder.avatar_Image);
        }catch (Exception e){
            holder.avatar_Image.setImageResource(R.drawable.img_doneemoji);
        }
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView avatar_Image;
        TextView tv_name, tv_notification, tv_timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_Image = itemView.findViewById(R.id.avatarIv_itemNotification);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name_notification);
            tv_notification = (TextView) itemView.findViewById(R.id.tv_message_notification);
            tv_timeStamp = (TextView) itemView.findViewById(R.id.tv_timeStamp);
        }
    }
}
