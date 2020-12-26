package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavType;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.quickdate.R;
import com.example.quickdate.listener.UserListener;
import com.example.quickdate.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class SwipeAct extends AppCompatActivity {

    private NavController navController;
    private User user;
    private String[] genders;
    private String[] lookingFor;
    UserListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        init();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void passVal(UserListener userListener) {
        this.userListener = userListener;
        userListener.getUser(user);
    }

    private void init() {
        genders = new String[]{"Male", "Female"};
        lookingFor = new String[]{"OneNight", "LongTerm", "Settlement"};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                FirebaseDatabase.getInstance()
                        .getReference("Users/" + genders[i] + "/" + lookingFor[j] + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
}