package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.app.ProgressDialog;
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
import com.example.quickdate.adapter.NotificationAdapter;
import com.example.quickdate.listener.Notification_RequestMatch_Listener;
import com.example.quickdate.model.Notification;
import com.example.quickdate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Notification extends Fragment implements Notification_RequestMatch_Listener {

    //recyclerview
    private RecyclerView recyclerView;
    private ArrayList<Notification> notificationArrayList;
    private NotificationAdapter notificationAdapter;
    private Notification_RequestMatch_Listener listener;

    // Model
    private User user;

    // DataReference to matchers and notifications nodes of realtime database
    DatabaseReference dataRef_matchers, dataRef_notifications;

    // ArrayList use for save list matchers of myUser
    private final ArrayList<String> listMatcher_Id = new ArrayList<>();

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
        listener = this;

        Activity_Home act = (Activity_Home) getActivity();
        user = act.getCurrentUser();

        // DataReference to matchers and notifications nodes of realtime database
        dataRef_matchers = FirebaseDatabase.getInstance().getReference("Matchers").child(user.getIdUser());
        dataRef_notifications = FirebaseDatabase.getInstance().getReference("Notifications");

        recyclerView = view.findViewById(R.id.notification_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        notificationArrayList = new ArrayList<>();

        getAllNotifications();

    }

    private void getAllNotifications() {
        // get all notifications from user
        Query query = FirebaseDatabase.getInstance().getReference("Notifications").orderByChild("receiverId").equalTo(user.getIdUser());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    notificationArrayList.add(ds.getValue(Notification.class));
                    notificationAdapter = new NotificationAdapter(getActivity(), notificationArrayList, user, listener);
                    recyclerView.setAdapter(notificationAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void unAcceptClick(Notification notification, int position, ProgressDialog pd) {
        // Show progressDialog
        pd.show();

        //find notification liked and delete it
        FirebaseDatabase.getInstance().getReference("Notifications")
                .orderByChild("type").equalTo(notification.getType())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            Notification nof = ds.getValue(Notification.class);
                            assert nof != null;

                            // get correctly notification
                            if (nof.getReceiverId().equals(notification.getReceiverId()) && nof.getSenderId().equals(notification.getSenderId())) {
                                DatabaseReference databaseReference = ds.getRef();
                                databaseReference.removeValue();
                                notificationArrayList.remove(position);
                                notificationAdapter.notifyDataSetChanged();

                                // after remove this notification, dismiss progressDialog
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void acceptClick(Notification notification, int position, ProgressDialog pd) {
        // show progressDialog
        pd.show();

        // find notification liked and delete it then save idMatcher to Matchers node in realtime

        dataRef_notifications.orderByChild("type").equalTo(notification.getType())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            Notification nof = ds.getValue(Notification.class);
                            assert nof != null;

                            // get correctly notification
                            if (nof.getReceiverId().equals(notification.getReceiverId()) && nof.getSenderId().equals(notification.getSenderId())) {

                                // remove this notification
                                DatabaseReference databaseReference = ds.getRef();
                                databaseReference.removeValue();
                                notificationArrayList.remove(position);
                                notificationAdapter.notifyDataSetChanged();

                                // save id matcher to Matchers node
                                FirebaseDatabase.getInstance().getReference("Matchers").child(notification.getReceiverId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        listMatcher_Id.clear();
                                        // add idMatcher was accept from acceptClick function
                                        listMatcher_Id.add(notification.getSenderId());

                                        for (DataSnapshot ds : snapshot.getChildren()) {

                                            //add list matcher from database to arrayList
                                            listMatcher_Id.add(ds.getValue(String.class));
                                        }

                                        // after add to arrayList, push again to database
                                        dataRef_matchers.setValue(listMatcher_Id);

                                        // After finish add data to database, dismiss progressDialog
                                        pd.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}