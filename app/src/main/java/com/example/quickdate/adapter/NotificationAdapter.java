package com.example.quickdate.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.listener.Notification_RequestMatch_Listener;
import com.example.quickdate.model.Notification;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final int NOTIFICATION_MESSAGE = 0;
    private static final int NOTIFICATION_LIKED = 1;

    // listener for acceptClick and unAcceptClick
    Notification_RequestMatch_Listener listener;

    Context context;
    ArrayList<Notification> notificationArrayList;
    User myUser;

    // Progress used for loading when data is save to the database
    ProgressDialog progressDialog;


    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList, User myUser, Notification_RequestMatch_Listener listener) {
        this.context = context;
        this.listener = listener;
        this.notificationArrayList = notificationArrayList;
        this.myUser = myUser;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    public int getItemViewType(int position) {
        if (notificationArrayList.get(position).getType().equals("Liked")) {
            return NOTIFICATION_LIKED;
        }
        else {
            return NOTIFICATION_MESSAGE;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == NOTIFICATION_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.laylout_notification_message, parent, false);

        } else{
            view = LayoutInflater.from(context).inflate(R.layout.layout_notification_liked, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        // on click expand
        PushDownAnim.setPushDownAnimTo(holder.ib_expand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Click expand icon will expand view, i executive for when expand icon is open or not
                if (holder.relativeLayout.getVisibility() == View.GONE) {

                    // this will open
                    TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                    holder.ib_expand.setImageResource(R.drawable.ic_arrow_bot);
                } else {
                    // this will close
                    TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                    holder.relativeLayout.setVisibility(View.GONE);
                    holder.ib_expand.setImageResource(R.drawable.ic_arrow_right);
                }
            }
        });


        // executive with layout notification_message and executive with layout notification_matcher
        if (holder.viewType == NOTIFICATION_LIKED) {

            // this view is type notification_matcher, i will executive 2 button is unAccept and accept
            // unAcceptClick
            PushDownAnim.setPushDownAnimTo(holder.ib_unAccept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.unAcceptClick(notification, position, progressDialog);
                }
            });

            // acceptClick
            PushDownAnim.setPushDownAnimTo(holder.ib_accept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.acceptClick(notification, position, progressDialog);
                }
            });

        } else if (holder.viewType == NOTIFICATION_MESSAGE) {

            // this view is type notification_message, i will executive editText and button send message
            replyMessage(holder, notification);
        }

        // get image and name of sender to load to View :V
        getInfoSender(holder, notification);

        // Set text notification
        holder.tv_notification.setText(notification.getNotification());

        // convert timer format to date time
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(notification.getTimeStamp()));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar).toString();

        // set datetime to tv_timeStamp
        holder.tv_timeStamp.setText(pTime);
    }

    private void getInfoSender(ViewHolder holder, Notification notification) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(myUser.getInfo().getGender().equals("Male") ? "Female" : "Male")
                .child(notification.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User senderUser = snapshot.getValue(User.class);

                // Set image
                try {
                    Picasso.get().load(senderUser.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(holder.avatar_Image);
                } catch (Exception e) {
                    holder.avatar_Image.setImageResource(R.drawable.img_doneemoji);
                }

                if (notification.getType().equals("Liked")) {
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

    public void replyMessage(ViewHolder holder, Notification notification) {
        // Show progressDialog
        progressDialog.show();

        // check et_message is empty?
        if (!TextUtils.isEmpty(holder.et_message.getText().toString())) {

            // Save this message to Chats node from Realtime database

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("senderId", myUser.getIdUser());
            hashMap.put("receiverId", notification.getSenderId());
            hashMap.put("message", holder.et_message.getText().toString().trim());
            hashMap.put("timestamp", "" + System.currentTimeMillis());
            hashMap.put("isSeen", false);
            FirebaseDatabase.getInstance().getReference("Chats").push().setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            holder.et_message.setText("");

                            // after send successfully, dismiss progressDialog
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // show toast to notification cant send empty text
            Toast.makeText(context, "Cant send empty message", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView avatar_Image;
        TextView tv_name, tv_notification, tv_timeStamp;
        ImageButton ib_expand;
        RelativeLayout relativeLayout;
        CardView cardView;

        // For layout_notification_liked
        ImageButton ib_unAccept, ib_accept;

        // For layout_notification message
        EditText et_message;
        ImageButton ib_send;

        int viewType;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;
            avatar_Image = itemView.findViewById(R.id.avatarIv_itemNotification);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name_notification);
            tv_notification = (TextView) itemView.findViewById(R.id.tv_message_notification);
            tv_timeStamp = (TextView) itemView.findViewById(R.id.tv_timeStamp);
            ib_expand = itemView.findViewById(R.id.ib_expand);
            relativeLayout = itemView.findViewById(R.id.rtl_expand);
            cardView = itemView.findViewById(R.id.cardView);

            if (viewType == NOTIFICATION_LIKED) {
                ib_unAccept = itemView.findViewById(R.id.ib_unAccept);
                ib_accept = itemView.findViewById(R.id.ib_accept);
            } else if (viewType == NOTIFICATION_MESSAGE) {
                et_message = itemView.findViewById(R.id.et_send_message_notification);
                ib_send = itemView.findViewById(R.id.btn_send_notification_message);
            }
        }
    }
}
