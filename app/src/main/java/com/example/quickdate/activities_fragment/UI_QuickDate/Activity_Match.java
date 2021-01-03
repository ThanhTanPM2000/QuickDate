package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quickdate.R;
import com.example.quickdate.model.User;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Match extends AppCompatActivity {

    CircleImageView imageUser, imageMatcher;
    TextView tv_match, tv_text;
    ImageButton ib_send;

    User user;
    User matcher;
    ArrayList<User> oppositeUsersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        init();
        doFunc();
    }

    private void init() {

        imageUser = findViewById(R.id.imageUser_matchAct);
        imageMatcher = findViewById(R.id.imageMatcher_matchAct);
        tv_match = findViewById(R.id.tv_matchAct);
        tv_text = findViewById(R.id.tv_text);
        ib_send = findViewById(R.id.ib_send);

        user = (User) getIntent().getSerializableExtra("User");
        matcher = (User) getIntent().getSerializableExtra("Matcher");
        oppositeUsersList = (ArrayList<User>) getIntent().getSerializableExtra("OppositeUsers");

        try {
            Picasso.get().load(user.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(imageUser);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_thumb).into(imageUser);
        }

        try {
            Picasso.get().load(matcher.getInfo().getImgAvt()).placeholder(R.drawable.ic_thumb).into(imageMatcher);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_thumb).into(imageMatcher);
        }

        tv_match.setText("You and " + matcher.getInfo().getNickname() + " just liked each other");

        if(user.getInfo().getGender().equals("Male")){
            tv_text.setText("Text her");
        }else{
            tv_text.setText("Text him");
        }
    }

    private void doFunc(){

        PushDownAnim.setPushDownAnimTo(ib_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Match.this, Activity_Chat.class);
                intent.putExtra("Matcher", matcher);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Activity_Match.this, Activity_Home.class);
        intent.putExtra("OppositeUsers", oppositeUsersList);
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }
}