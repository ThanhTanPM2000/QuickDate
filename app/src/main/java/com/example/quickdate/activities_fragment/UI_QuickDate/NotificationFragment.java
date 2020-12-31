package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickdate.R;
import com.example.quickdate.adapter.MatcherAdapter;
import com.example.quickdate.adapter.NotificationAdapter;
import com.example.quickdate.model.Notification;
import com.example.quickdate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    //recyclerview
    private RecyclerView recyclerView;
    private ArrayList<Notification> notificationArrayList;
    private NotificationAdapter notificationAdapter;

    // Model
    private User user;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);
        return view;
    }


    private void init(View view) {
        SwipeAct act = (SwipeAct) getActivity();
        user = act.getCurrentUser();

        recyclerView = view.findViewById(R.id.notification_rv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        notificationArrayList = new ArrayList<>();

        getAllNotifications();

    }

    private void getAllNotifications() {
        // get all notifications from user
        Query query = FirebaseDatabase.getInstance().getReference("Notifications").orderByChild("received").equalTo(user.getIdUser());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    notificationArrayList.add(ds.getValue(Notification.class));

                    notificationAdapter = new NotificationAdapter(getActivity(), notificationArrayList);
                    recyclerView.setAdapter(notificationAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}