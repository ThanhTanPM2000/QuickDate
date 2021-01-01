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
import com.example.quickdate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;
    private User myUser;


    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList, User myUser){
        this.context = context;
        this.notificationArrayList = notificationArrayList;
        this.myUser = myUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        // get image and name of sender from senderId Notification
        getInfoSender(holder, notification);

        // Set text notification
        holder.tv_notification.setText(notification.getNotification());

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(notification.getTimeStamp()));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar).toString();
        holder.tv_timeStamp.setText(pTime);
    }

    private void getInfoSender(ViewHolder holder, Notification notification) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(myUser.getInfo().getGender().equals("Male")?"Female":"Male")
                .child(notification.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User senderUser = snapshot.getValue(User.class);

                // Set image
                try{
                    Picasso.get().load(senderUser.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(holder.avatar_Image);
                }catch (Exception e){
                    holder.avatar_Image.setImageResource(R.drawable.img_doneemoji);
                }

                if(notification.getType().equals("Liked")){
                    PushDownAnim.setPushDownAnimTo(holder.itemView).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                // Set Name
                holder.tv_name.setText(senderUser.getInfo().getNickname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
