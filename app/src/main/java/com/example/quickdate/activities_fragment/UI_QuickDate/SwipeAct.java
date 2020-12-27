package com.example.quickdate.activities_fragment.UI_QuickDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

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


public class SwipeAct extends AppCompatActivity {

    private NavController navController;
    private User user;
    private User user2;
    private Users users;
    private String[] genders;
    private String[] lookingFor;
    UserListener userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        init();

    }

    public void passValUser(UserListener userListener) {
        this.userListener = userListener;
        userListener.getUser(user);
    }

    public void passValUsers(UserListener userListener) {
        this.userListener = userListener;
        userListener.getUsers(users);
    }

    private void init() {
        users = new Users();
        genders = new String[]{"Male", "Female"};
        lookingFor = new String[]{"OneNight", "LongTerm", "Settlement"};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                FirebaseDatabase.getInstance()
                        .getReference("Users/" + genders[i] + "/" + lookingFor[j] + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            user = snapshot.getValue(User.class);
                            BottomNavigationView navView = findViewById(R.id.nav_view);
                            navController = Navigation.findNavController(SwipeAct.this, R.id.nav_host_fragment);
                            NavigationUI.setupWithNavController(navView, navController);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void getUsers(){
        FirebaseDatabase.getInstance().getReference("Users/" + (user.getInfo().getGender().equals("Male") ? "Female/" : "Male/")).child(user.getLookingFor().getLooking()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    user2 = postSnapshot.getValue(User.class);
                    if(user2.getInfo().getAge() >= user.getLookingFor().getMin_age() || user2.getInfo().getAge() <= user.getLookingFor().getMax_age()){
                        users.getUsers().add(user2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}