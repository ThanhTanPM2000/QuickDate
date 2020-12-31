package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.model.Matcher;
import com.example.quickdate.model.User;
import com.example.quickdate.model.OppositeUsers;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;


public class SwipeAct extends AppCompatActivity {

    private User user;
    public TextView tv_head_title;
    private ImageButton btn_setting, btn_notification;
    private BottomNavigationView navView;
    private View dialogFragment, navBotFragment;
    private Boolean isNotificationClick;
    private OppositeUsers myOppositeUsers;

    // index menu default
    private int indexMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        init();
        doFunction();
    }

    private void init() {
        navView = findViewById(R.id.nav_view);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_notification = (ImageButton) findViewById(R.id.btn_notification);
        dialogFragment = (View) findViewById(R.id.dialog_fragment);
        navBotFragment = (View) findViewById(R.id.nav_host_fragment);
        isNotificationClick = true;

        user = (User) getIntent().getSerializableExtra("User");
        myOppositeUsers = (OppositeUsers) getIntent().getSerializableExtra("OppositeUsers");

        indexMenu = getIntent().getIntExtra("MenuDefault", 1);
    }

    private void doFunction() {
        //btn setting click
        PushDownAnim.setPushDownAnimTo(btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Matcher").child(user.getIdUser()).setValue(myOppositeUsers.getUsers()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SwipeAct.this, "add matcher successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // btn notification click
        PushDownAnim.setPushDownAnimTo(btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNotificationClick){
                    dialogFragment.setVisibility(View.VISIBLE);
                    navBotFragment.setAlpha(0.4f);
                    btn_notification.setImageResource(R.drawable.ic_close);
                    btn_setting.setAlpha(0.4f);
                    btn_setting.setClickable(false);
                    navView.setVisibility(View.GONE);
                    tv_head_title.setAlpha(0.4f);
                    isNotificationClick = false;
                    loadFragment(new NotificationFragment(), R.id.dialog_fragment);
                }else{
                    dialogFragment.setVisibility(View.GONE);
                    navBotFragment.setAlpha(1f);
                    btn_notification.setImageResource(R.drawable.ic_ring);
                    btn_setting.setAlpha(1f);
                    btn_setting.setClickable(true);
                    navView.setVisibility(View.VISIBLE);
                    tv_head_title.setAlpha(1f);
                    isNotificationClick = true;
                }
            }
        });

        if(indexMenu == 0){
            loadFragment(new MyProfileFragment(), R.id.nav_host_fragment);
        }else if (indexMenu == 1){
            loadFragment(new SwiperFragment(), R.id.nav_host_fragment);
        }else {
            loadFragment(new MatchesFragment(), R.id.nav_host_fragment);
        }

        // connect bottom navigation with menu
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().getItem(indexMenu).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Bundle bundle=new Bundle();
            bundle.putSerializable("User", myOppositeUsers);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MyProfileFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new SwiperFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment = new MatchesFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment, int id) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public User getCurrentUser() {
        return user;
    }

    public OppositeUsers getAllOppositeUsers() {
        return myOppositeUsers;
    }
}