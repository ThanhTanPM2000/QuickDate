package com.example.quickdate.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.listener.ImagesListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageRegisterAdapter extends RecyclerView.Adapter<ImageRegisterAdapter.ViewHolder> {
    private Context context;
    private final ArrayList<Uri> images;
    private static ImagesListener imagesListener;

    public ImageRegisterAdapter(ArrayList<Uri> images, ImagesListener imagesListener){
        this.images = images;
        ImageRegisterAdapter.imagesListener = imagesListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View imageView = inflater.inflate(R.layout.layout_container_image, parent, false);


        return new ViewHolder(imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = images.get(position).toString();

        Picasso.get().load(image).noFade().into(holder.riv, new Callback() {
            @Override
            public void onSuccess() {
                holder.riv.animate().setDuration(300).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        holder.bindImage(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        RoundedImageView riv;
        ImageView iv_removeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            riv = (RoundedImageView) this.itemView.findViewById(R.id.roundedImageView);
            iv_removeImage = (ImageView) this.itemView.findViewById(R.id.iv_removeImage);
        }

        public void bindImage(int position){
            PushDownAnim.setPushDownAnimTo(iv_removeImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagesListener.onImageClicked(position);
                }
            });
        }
    }
}
