package com.example.quickdate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.model.User;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class MatcherAdapter extends  RecyclerView.Adapter<MatcherAdapter.ViewHolder>{
    Context context;
    ArrayList<User> userArrayList;

    public MatcherAdapter(Context context, ArrayList<User> userArrayList){
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matcher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userImage = userArrayList.get(position).getInfo().getImgAvt();
        String userName = userArrayList.get(position).getInfo().getNickname();

        holder.mNameTv.setText(userName);

        try{
            Picasso.get().load(userImage).placeholder(R.drawable.ic_thumb).into(holder.mAvatarIv);
        }catch (Exception e){

        }

        PushDownAnim.setPushDownAnimTo(holder.itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarIv;
        TextView mNameTv, mMessaged;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarIv = itemView.findViewById(R.id.avatarIv_itemUser);
            mNameTv = itemView.findViewById(R.id.tv_name_itemUser);
            mMessaged = itemView.findViewById(R.id.tv_message_itemUser);
        }
    }
}
