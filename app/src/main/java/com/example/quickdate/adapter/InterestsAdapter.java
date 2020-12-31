package com.example.quickdate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.listener.InterestsListener;
import com.example.quickdate.model.Interest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    private final ArrayList<Interest> Interests;
    private static InterestsListener interestsListener;
    private Boolean status;

    public InterestsAdapter(ArrayList<Interest> interests, Boolean status){
        this.status = status;
        this.Interests = interests;
    }

    public InterestsAdapter(ArrayList<Interest> interests, InterestsListener interestsListener, Boolean status){
        this.Interests = interests;
        InterestsAdapter.interestsListener = interestsListener;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(Interests.get(position).getStatus()){
            holder.iv_status.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }else{
            holder.iv_status.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }

        holder.tv_interestName.setText(Interests.get(position).getName());
        Picasso.get().load(Interests.get(position).getImage()).into(holder.roundedImageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.roundedImageView.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        if(!status){
            holder.bindStatusInterest(position);
        }
    }

    @Override
    public int getItemCount() {
        return Interests.size();
    }


    static class  ViewHolder extends RecyclerView.ViewHolder{
        View view;
        RoundedImageView roundedImageView;
        TextView tv_interestName;
        ImageView iv_status;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView_interestsAct);
            tv_interestName = (TextView) view.findViewById(R.id.tv_interestAct);
            iv_status = (ImageView) view.findViewById(R.id.iv_checked_containerInterest);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox_interestsAct);
        }

        public void bindStatusInterest(int position){
            PushDownAnim.setPushDownAnimTo(roundedImageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                    if(checkBox.isChecked()){
                        iv_status.setVisibility(View.VISIBLE);
                    }else{
                        iv_status.setVisibility(View.GONE);
                    }
                    interestsListener.onInterestsClicked(position, checkBox.isChecked());
                }
            });
        }
    }
}
