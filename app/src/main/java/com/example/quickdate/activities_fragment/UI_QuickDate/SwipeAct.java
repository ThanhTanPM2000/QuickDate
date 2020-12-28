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

import com.example.quickdate.R;
import com.example.quickdate.listener.UserListener;
import com.example.quickdate.model.User;
import com.example.quickdate.model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;


public class SwipeAct extends AppCompatActivity {

    private User user;
    private TextView tv_head_title;
    private ImageButton btn_setting, btn_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        init();
        doFunction();
    }

    private void doFunction() {
        //btn setting click
        PushDownAnim.setPushDownAnimTo(btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // btn notification click
        PushDownAnim.setPushDownAnimTo(btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loadFragment(new SwiperFragment());
        // connect bottom navigation with menu
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().getItem(1).setChecked(true);

        // Search user in firebase realtime database
        findUserInfo();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MyProfileFragment();
                    loadFragment(fragment);
                    tv_head_title.setText("");
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new SwiperFragment();
                    loadFragment(fragment);
                    tv_head_title.setText("Quick Date");
                    return true;
                case R.id.navigation_notifications:
                    fragment = new MatchesFragment();
                    loadFragment(fragment);
                    tv_head_title.setText("Matches");
                    return true;
            }
            return false;
        }
    };

    private void init() {
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_notification = (ImageButton) findViewById(R.id.btn_notification);
    }

    // Pass value user for fragment which need this user
    public void passValUser(UserListener userListener) {
        userListener.getUser(user);
    }

    private void findUserInfo() {
        String[] genders = new String[]{"Male", "Female"};
        String[] lookingFor = new String[]{"OneNight", "LongTerm", "Settlement"};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                FirebaseDatabase.getInstance()
                        .getReference("Users/" + genders[i] + "/" + lookingFor[j] + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            user = snapshot.getValue(User.class);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}