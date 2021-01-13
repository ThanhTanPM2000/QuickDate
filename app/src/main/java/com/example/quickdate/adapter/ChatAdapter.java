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
import com.example.quickdate.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT =1;
    private ArrayList<Chat> chatArrayList;
    private String imageUrl;

    private Context context;

    private FirebaseUser firebaseUser;

    public ChatAdapter(Context context, ArrayList<Chat> chatArrayList, String imageUrl){
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.imageUrl = imageUrl;
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatArrayList.get(position).getSenderId().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.messageTv.setText(chatArrayList.get(position).getMessage());

        Calendar cal = Calendar.getInstance(Locale.CHINESE);
        cal.setTimeInMillis(Long.parseLong(chatArrayList.get(position).getTimestamp()));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        holder.timeTv.setText(dateTime);

        try {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_thumb).into(holder.circleImageView);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_thumb).into(holder.circleImageView);
        }

        if(position == chatArrayList.size()-1){
            if(chatArrayList.get(position).isSeen()){
                holder.isSeenTv.setText("Seen");
            }else{
                holder.isSeenTv.setText("Delivered");
            }

        }else{
            holder.isSeenTv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView messageTv, timeTv, isSeenTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.message_chat);
            timeTv = itemView.findViewById(R.id.timeStamp_chat);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);

        }
    }
}
