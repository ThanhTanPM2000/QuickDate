package com.example.quickdate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_HerProfile;
import com.example.quickdate.model.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.yuyakaido.android.cardstackview.CardStackListener;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    Context context;
    private ArrayList<User> userArrayList;

    public CardStackAdapter(Context context ,ArrayList<User> myOppositeUsers) {
        this.context = context;
        this.userArrayList = myOppositeUsers;
    }

    public ArrayList<User> getUsers() {
        return userArrayList;
    }

    public void setUsers(ArrayList<User> users) {
        userArrayList = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_swiper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        String[] arr = user.getInfo().getNickname().split(" ");
        if(arr[arr.length -1].length() > 8){
            holder.tv_name.setText(arr[arr.length -1].substring(0, 8) + "...,");
        }else{
            holder.tv_name.setText(arr[arr.length-1] + ",");
        }
        holder.tv_age.setText(user.getInfo().getAge() + "");
        holder.tv_height.setText(user.getInfo().getHeight() + "cm");
        Picasso.get().load(user.getInfo().getImgAvt()).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.imageView.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        PushDownAnim.setPushDownAnimTo(holder.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_HerProfile.class);
                intent.putExtra("User", userArrayList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getUsers().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_age, tv_height;
        RoundedImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View view){
            tv_name = (TextView) view.findViewById(R.id.item_name);
            tv_age = (TextView) view.findViewById(R.id.item_age);
            tv_height = (TextView) view.findViewById(R.id.item_height);
            imageView = (RoundedImageView) view.findViewById(R.id.item_image);
        }
    }

}
