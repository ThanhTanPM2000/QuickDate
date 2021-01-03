package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Main;
import com.example.quickdate.adapter.ChatAdapter;
import com.example.quickdate.model.Chat;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class Activity_Chat extends AppCompatActivity {


    // Component of Chat Activity
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView nameTv, statusTv, typingTv;
    private EditText messageEt;
    private ImageButton ib_send, ib_backAct;

    // data matcher ID
    private User myUser;
    private User matcher;

    // For checking if user has seen message or not
    ValueEventListener seenListener;
    DatabaseReference useRefForSeen;

    // For checking if user has seen message or not
    ValueEventListener matcherListener;
    DatabaseReference refMatcherInfo;

    ArrayList<Chat> chatArrayList;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        doFunc();
    }

    private void init() {

        // Init toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        // Init View
        recyclerView = findViewById(R.id.recyclerView_chatActivity);
        circleImageView = findViewById(R.id.matcher_avatar);
        nameTv = findViewById(R.id.matcher_name);
        statusTv = findViewById(R.id.matcher_status);
        messageEt = findViewById(R.id.messageEt_chatAct);
        ib_backAct = findViewById(R.id.backAct_chatAct);
        ib_send = findViewById(R.id.send_message_chatAct);
        typingTv = findViewById(R.id.tv_typing);

        // get model data
        myUser = (User) getIntent().getSerializableExtra("User");
        matcher = (User) getIntent().getSerializableExtra("Matcher");

        nameTv.setText(matcher.getInfo().getNickname());
        try {
            Picasso.get().load(matcher.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(circleImageView);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_thumb).into(circleImageView);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //recyclerView properies
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        seenListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        readMessages();
        seenMessages();
    }

    private void seenMessages() {
        useRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = useRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReceiverId().equals(myUser.getIdUser()) && chat.getSenderId().equals(matcher.getIdUser())) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);

                        ds.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        useRefForSeen.removeEventListener(seenListener);
    }

    private void readMessages() {
        chatArrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReceiverId().equals(myUser.getIdUser()) && chat.getSenderId().equals(matcher.getIdUser()) ||
                            chat.getReceiverId().equals(matcher.getIdUser()) && chat.getSenderId().equals(myUser.getIdUser())) {
                        chatArrayList.add(chat);
                    }

                    adapter = new ChatAdapter(Activity_Chat.this, chatArrayList, matcher.getInfo().getImgAvt());
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doFunc() {

        PushDownAnim.setPushDownAnimTo(ib_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToDatabase();
            }
        });

        PushDownAnim.setPushDownAnimTo(ib_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.toString().trim().length() == 0) {
                    checkTypingStatus("NoOne");
                } else {
                    checkTypingStatus(matcher.getIdUser());
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendMessageToDatabase() {
        String message = messageEt.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(Activity_Chat.this, "Cant not send empty message", Toast.LENGTH_SHORT).show();
        } else {

            String timeStamp = String.valueOf(System.currentTimeMillis());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("senderId", myUser.getIdUser());
            hashMap.put("receiverId", matcher.getIdUser());
            hashMap.put("message", message);
            hashMap.put("timestamp", timeStamp);
            hashMap.put("isSeen", false);

            FirebaseDatabase.getInstance().getReference("Chats").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    hashMap.clear();
                    hashMap.put("senderId", myUser.getIdUser());
                    hashMap.put("receiverId", matcher.getIdUser());
                    hashMap.put("type", "Messaged");
                    hashMap.put("notification", message);
                    hashMap.put("timeStamp", timeStamp);
                    hashMap.put("isSeen", false);

                    FirebaseDatabase.getInstance().getReference("Notifications").push().setValue(hashMap);
                }
            });

            messageEt.setText("");
        }
    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(myUser.getInfo().getGender())
                .child(myUser.getIdUser())
                .child("statusOnline");

        if (firebaseUser == null) {
            databaseReference.setValue("" + System.currentTimeMillis());
            Intent intent = new Intent(Activity_Chat.this, Activity_Main.class);
            startActivity(intent);
            finish();
        } else {
            databaseReference.setValue("Online");
        }
    }

    private void checkOnlineStatus() {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(myUser.getInfo().getGender().equals("Male")?"Female":"Male")
                .child(matcher.getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String matcherStatusOnline = "" + snapshot.child("statusOnline").getValue();

                String typingTo = "" + snapshot.child("typingTo").getValue();

                if (typingTo.equals(myUser.getIdUser())) {
                    typingTv.setText(matcher.getInfo().getNickname() + " typing...");
                    typingTv.setVisibility(View.VISIBLE);
                } else {
                    typingTv.setText("");
                    typingTv.setVisibility(View.INVISIBLE);
                }

                if (matcherStatusOnline.equals("Online")) {
                    statusTv.setText(matcherStatusOnline);
                    statusTv.setTextColor(getResources().getColor(R.color.green, null));
                } else {
                    statusTv.setText("Offline");
                    statusTv.setTextColor(getResources().getColor(R.color.red, null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkTypingStatus(String typing) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(myUser.getInfo().getGender())
                .child(myUser.getIdUser())
                .child("typingTo").setValue(typing);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus();
        super.onStart();
    }
}