package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Main;
import com.example.quickdate.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;


public class Activity_Home extends AppCompatActivity {

    private User user;
    public TextView tv_head_title;
    private ImageButton btn_setting, btn_notification;
    private BottomNavigationView navView;
    private View dialogFragment, navBotFragment;
    private Boolean isNotificationClick;
    private ArrayList<User> myOppositeUsers;

    // index menu default
    private int indexMenu;

    private String statusOnline;

    // Navigation View
    private DrawerLayout drawerLayout;
    private NavigationView navigationView_setting, navigationView_notification;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        doFunction();
    }

    private void init() {
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout_setting);
        navigationView_setting = findViewById(R.id.nav_setting_swipeAct);
        navigationView_setting.setItemIconTintList(null);
        navigationView_notification = findViewById(R.id.nav_notification_swipeAct);
        navigationView_setting.bringToFront();
        navigationView_notification.bringToFront();

        tv_head_title = findViewById(R.id.tv_head_title);
        btn_setting = findViewById(R.id.btn_setting);
        btn_notification = findViewById(R.id.btn_notification);
        navBotFragment = findViewById(R.id.nav_host_fragment);

        user = (User) getIntent().getSerializableExtra("User");
        myOppositeUsers = (ArrayList<User>) getIntent().getSerializableExtra("OppositeUsers");


        indexMenu = getIntent().getIntExtra("MenuDefault", 1);
    }

    private void doFunction() {
        navigationRightSelectedItem();

        //btn setting click
        PushDownAnim.setPushDownAnimTo(btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.closeDrawer(GravityCompat.END);
                }

                /*FirebaseDatabase.getInstance().getReference("Matcher").child(user.getIdUser()).setValue(myOppositeUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Activity_Home.this, "add matcher successfully", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        // btn notification click
        PushDownAnim.setPushDownAnimTo(btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    loadFragment(new Fragment_Notification(), R.id.fragment_notifications);
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        if(indexMenu == 0){
            loadFragment(new Fragment_MyProfile(), R.id.nav_host_fragment);
        }else if (indexMenu == 1){
            loadFragment(new Fragment_Swiper(), R.id.nav_host_fragment);
        }else {
            loadFragment(new Fragment_Matches(), R.id.nav_host_fragment);
        }

        // connect bottom navigation with menu
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().getItem(indexMenu).setChecked(true);
    }



    private void navigationRightSelectedItem(){
        navigationView_setting.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_watch_ads:
                        break;
                    case R.id.navigation_notifications:
                        break;
                    case R.id.navigation_darkmode:
                        break;
                    case R.id.navigation_changeType:
                        break;
                    case R.id.navigation_logout:
                        break;
                    case R.id.navigation_about:
                        break;
                    case R.id.navigation_faq:
                        break;
                }

                return false;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Bundle bundle=new Bundle();
            bundle.putSerializable("User", myOppositeUsers);
            switch (item.getItemId()) {
                case R.id.navigation_myProfile:
                    fragment = new Fragment_MyProfile();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    return true;
                case R.id.navigation_swipe:
                    fragment = new Fragment_Swiper();
                    loadFragment(fragment, R.id.nav_host_fragment);
                    return true;
                case R.id.navigation_matches:
                    fragment = new Fragment_Matches();
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

    public ArrayList<User> getAllOppositeUsers() {
        return myOppositeUsers;
    }

    public String getStatusOnline(){
        return statusOnline;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
        else{

        }
    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            checkOnlineStatus("Offline");
            Intent intent = new Intent(Activity_Home.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }else{
            checkOnlineStatus("Online");
        }
    }

    private void checkOnlineStatus(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StatusOnline").child(user.getIdUser());
        databaseReference.setValue(status);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


}