package com.example.quickdate.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.quickdate.model.User;

import java.util.ArrayList;

public class UserDiffCallBack extends DiffUtil.Callback {

    private ArrayList<User> old;
    private ArrayList<User> newer;

    public UserDiffCallBack(ArrayList<User> old, ArrayList<User> newer) {
        this.old = old;
        this.newer = newer;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return newer.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getIdUser().equals(newer.get(newItemPosition).getIdUser());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == newer.get(newItemPosition);
    }
}
