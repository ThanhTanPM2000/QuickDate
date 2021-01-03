package com.example.quickdate.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_Chat;
import com.example.quickdate.model.User;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MatcherAdapter extends  RecyclerView.Adapter<MatcherAdapter.ViewHolder>{
    Context context;
    ArrayList<User> userArrayList;
    private User myUser;

    public MatcherAdapter(Context context, ArrayList<User> userArrayList){
        this.context = context;
        this.userArrayList = userArrayList;
    }

    public MatcherAdapter(Context context, ArrayList<User> userArrayList, User user){
        this.context = context;
        this.userArrayList = userArrayList;
        myUser = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matcher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get uid matcher
        User marcher = userArrayList.get(position);

        String userImage = marcher.getInfo().getImgAvt();
        String userName = marcher.getInfo().getNickname();

        holder.mNameTv.setText(userName);
        if(marcher.getStatusOnline().equals("Online")){
            holder.imageView_status.setVisibility(View.VISIBLE);
            holder.mTimeOffline.setText("");
        }else{
            holder.imageView_status.setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance(Locale.CHINESE);
            cal.setTimeInMillis(System.currentTimeMillis() - Long.parseLong(marcher.getStatusOnline()));
            String dateTime = DateFormat.format("hh:mm", cal).toString() + " ago";

            holder.mTimeOffline.setText(dateTime);
        }

        try{
            Picasso.get().load(userImage).placeholder(R.drawable.ic_thumb).into(holder.mAvatarIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_thumb).into(holder.mAvatarIv);
        }

        PushDownAnim.setPushDownAnimTo(holder.itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Chat.class);
                intent.putExtra("MatcherId", userArrayList.get(position).getIdUser());
                intent.putExtra("User", myUser);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarIv, imageView_status;
        TextView mNameTv, mMessaged, mTimeOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarIv = itemView.findViewById(R.id.avatarIv_itemUser);
            mNameTv = itemView.findViewById(R.id.tv_name_itemUser);
            mMessaged = itemView.findViewById(R.id.tv_message_itemUser);
            imageView_status = itemView.findViewById(R.id.image_status);
            mTimeOffline = itemView.findViewById(R.id.timeOffline_itemMatcher);
        }
    }
}
