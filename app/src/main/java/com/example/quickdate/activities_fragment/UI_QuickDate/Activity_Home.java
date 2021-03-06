package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_BioPhotos;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Interests;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Login;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Main;
import com.example.quickdate.activities_fragment.UI_StartLoginRegister.Activity_Type;
import com.example.quickdate.model.Notification;
import com.example.quickdate.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Objects;


public class Activity_Home extends AppCompatActivity {

    private User user;
    public TextView tv_head_title;
    private ImageButton btn_setting, btn_notification;
    private BottomNavigationView navView;
    private CardView notification_Counter;

    // index menu default
    private int indexMenu;

    private ValueEventListener isSeenNotificationListener;
    private DatabaseReference refIsSeenNotification;

    // Navigation View
    private DrawerLayout drawerLayout;
    private NavigationView navigationView_setting, navigationView_notification;

    String sUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        if (user == null) {
            findCurrentUser();
        } else {
            if (indexMenu == 0) {
                loadFragment(new Fragment_MyProfile(), R.id.nav_host_fragment);
            } else if (indexMenu == 1) {
                loadFragment(new Fragment_Swiper(), R.id.nav_host_fragment);
            } else if (indexMenu == 2) {
                loadFragment(new Fragment_Matches(), R.id.nav_host_fragment);
            }
        }

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
        notification_Counter = findViewById(R.id.notification_counter);

        user = (User) getIntent().getSerializableExtra("User");

        indexMenu = getIntent().getIntExtra("MenuDefault", 1);
    }

    private void doFunction() {
        navigationRightSelectedItem();

        //btn setting click
        PushDownAnim.setPushDownAnimTo(btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        // btn notification click
        PushDownAnim.setPushDownAnimTo(btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    refIsSeenNotification.removeEventListener(isSeenNotificationListener);

                    // set isSeen = true when btn_notification click
                    refIsSeenNotification.orderByChild("receiverId").equalTo(user.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Notification notification = ds.getValue(Notification.class);
                                String s = ds.getKey();
                                assert notification != null;
                                if (!notification.isSeen()) {
                                    refIsSeenNotification.child(Objects.requireNonNull(ds.getKey())).child("isSeen").setValue(true);
                                }
                            }
                            loadFragment(new Fragment_Notification(), R.id.fragment_notifications);
                            drawerLayout.openDrawer(GravityCompat.END);
                            checkIsSeenNotification();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //set notification counter setVisibility is gone
                    notification_Counter.setVisibility(View.GONE);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        // connect bottom navigation with menu
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().getItem(indexMenu).setChecked(true);
    }

    private void navigationRightSelectedItem() {
        navigationView_setting.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_changeType:
                        Intent intent = new Intent(Activity_Home.this, Activity_Type.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("User", user);
                        intent.putExtra("isRegisterInfo", true);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.navigation_logout:
                        FirebaseAuth.getInstance().signOut();
                        checkOnlineStatus("" + System.currentTimeMillis());
                        Intent intent1 = new Intent(Activity_Home.this, Activity_Login.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
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

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
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

    private void findCurrentUser() {
        String[] genders = new String[]{"Male", "Female"};
        for (int i = 0; i < 2; i++) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(genders[i]).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                        checkUserStatus();
                        checkIsSeenNotification();
                        if (indexMenu == 0) {
                            loadFragment(new Fragment_MyProfile(), R.id.nav_host_fragment);
                        } else if (indexMenu == 1) {
                            loadFragment(new Fragment_Swiper(), R.id.nav_host_fragment);
                        } else if (indexMenu == 2) {
                            loadFragment(new Fragment_Matches(), R.id.nav_host_fragment);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public User getCurrentUser() {
        return user;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
        else {

        }
    }

    private void checkUserStatus() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            sUID = firebaseUser.getUid();
            checkOnlineStatus("Online");
        } else {
            checkOnlineStatus("" + System.currentTimeMillis());
            Intent intent = new Intent(Activity_Home.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkOnlineStatus(String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender()).child(user.getIdUser());
        databaseReference.child("statusOnline").setValue(status);
    }

    @Override
    protected void onStart() {
        findCurrentUser();

        super.onStart();
    }

    private void checkIsSeenNotification() {
        refIsSeenNotification = FirebaseDatabase.getInstance().getReference("Notifications");
        isSeenNotificationListener = refIsSeenNotification.orderByChild("receiverId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Notification nof = ds.getValue(Notification.class);
                            assert nof != null;
                            if (!nof.isSeen()) {
                                notification_Counter.setVisibility(View.VISIBLE);
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
        refIsSeenNotification.removeEventListener(isSeenNotificationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}