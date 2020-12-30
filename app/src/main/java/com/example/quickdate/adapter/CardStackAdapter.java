package com.example.quickdate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private ArrayList<User> Users;

    public CardStackAdapter(com.example.quickdate.model.Users myUsers) {
        this.Users = myUsers.getUsers();
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<User> users) {
        Users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_swiper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = Users.get(position);
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
