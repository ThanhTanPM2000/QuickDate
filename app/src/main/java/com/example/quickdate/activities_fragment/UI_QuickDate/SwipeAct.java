package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.listener.UserListener;
import com.example.quickdate.model.User;
import com.example.quickdate.model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;


public class SwipeAct extends AppCompatActivity {

    private User user;
    private TextView tv_head_title;
    private ImageButton btn_setting, btn_notification;
    private BottomNavigationView navView;
    private View dialogFragment, navBotFragment;
    private Boolean isNotificationClick;
    private Users myUsers;

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

        indexMenu = getIntent().getIntExtra("MenuDefault", 1);
    }

    private void doFunction() {
        //btn setting click
        PushDownAnim.setPushDownAnimTo(btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
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
            bundle.putSerializable("User", myUsers);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MyProfileFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    tv_head_title.setText("");
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new SwiperFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    tv_head_title.setText("Quick Date");
                    return true;
                case R.id.navigation_notifications:
                    fragment = new MatchesFragment();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    tv_head_title.setText("Matches");
                    return true;
            }
            return false;
        }
    };

    // Pass value user for fragment which need this user
    public void passValUser(UserListener userListener) {
        userListener.getUser(user);
    }



    private void loadFragment(Fragment fragment, int id) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}