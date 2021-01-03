package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Notification extends Fragment implements Notification_RequestMatch_Listener {

    //recyclerview
    private RecyclerView recyclerView;
    private ArrayList<Notification> notificationArrayList;
    private NotificationAdapter notificationAdapter;
    private Notification_RequestMatch_Listener listener;

    // Model
    private User user;
    private User matcher;

    ValueEventListener notificationsListener;

    // DataReference to matchers and notifications nodes of realtime database
    DatabaseReference dataRef_matchers, dataRef_notifications;

    // ArrayList use for save list matchers of myUser
    private ArrayList<String> listMatcher_Id;

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
        listMatcher_Id = new ArrayList<>();

    }

    @Override
    public void onStart() {
        getAllNotifications();
        super.onStart();
    }

    @Override
    public void onResume() {
        getAllNotifications();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        dataRef_notifications.removeEventListener(notificationsListener);
    }

    private void getAllNotifications() {
        // get all notifications from user
        Query query = dataRef_notifications.orderByChild("receiverId").equalTo(user.getIdUser());
        notificationsListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationArrayList.clear();
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
        dataRef_notifications
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

                                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender().equals("Male")?"Female":"Male")
                                        .child(notification.getSenderId()).child("matchers");

                                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        listMatcher_Id.clear();
                                        // add idMatcher was accept from acceptClick function
                                        listMatcher_Id.add(notification.getReceiverId());

                                        for (DataSnapshot ds : snapshot.getChildren()) {

                                            //add list matcher from database to arrayList
                                            listMatcher_Id.add(ds.getValue(String.class));
                                        }

                                        // after add to arrayList, push again to database
                                        dataRef.setValue(listMatcher_Id);

                                        String timeStamp = "" + System.currentTimeMillis();

                                        HashMap<Object, String> hashMap = new HashMap<>();
                                        hashMap.put("senderId", user.getIdUser());
                                        hashMap.put("receiverId", notification.getSenderId());
                                        hashMap.put("type", "Matched");
                                        hashMap.put("notification", user.getInfo().getNickname() + " accept your match request");
                                        hashMap.put("timeStamp", timeStamp);

                                        FirebaseDatabase.getInstance().getReference("Notifications").push().setValue(hashMap);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                // save id matcher to Matchers node
                                DatabaseReference dataRef2 = FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender())
                                        .child(notification.getReceiverId()).child("matchers");
                                dataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        dataRef2.setValue(listMatcher_Id);

                                        // remove this matcher out of oppositeUser
                                        matcher = new User();
                                        FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender().equals("Male")?"Female":"Male")
                                                .child(notification.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                matcher = snapshot.getValue(User.class);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        // After finish add data to database, dismiss progressDialog
                                        pd.dismiss();

                                        Intent intent = new Intent(getActivity(), Activity_Match.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        intent.putExtra("User", user);
                                        intent.putExtra("Matcher", matcher);
                                        startActivity(intent);
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