package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quickdate.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Chat extends AppCompatActivity {


    // Component of Chat Activity
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView nameTv, statusTv;
    private EditText messageEt;
    private ImageButton ib_uploadFile, ib_uploadImage, ib_send, ib_backAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.recyclerView_chatActivity);
        circleImageView = findViewById(R.id.matcher_avatar);
        nameTv = findViewById(R.id.matcher_name);
        statusTv = findViewById(R.id.matcher_status);
        messageEt = findViewById(R.id.messageEt_chatAct);
        ib_uploadFile = findViewById(R.id.upload_file_chatAct);
        ib_backAct = findViewById(R.id.backAct_chatAct);
        ib_send = findViewById(R.id.send_message_chatAct);
        ib_uploadImage = findViewById(R.id.upload_image_chatAct);
    }
}