package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.Activity_Home;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.listener.InterestsListener;
import com.example.quickdate.model.User;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class Activity_Interests extends AppCompatActivity implements InterestsListener {
    private InterestsAdapter interestsAdapter;
    private RecyclerView recyclerView;
    private ImageView iv_backAct, iv_submit;

    // Model
    private User user;
    private ArrayList<User> oppositeUsers;

    private Boolean isRegisterInfo;

    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        init();
        doFunctionInAct();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView_interestsAct);
        iv_backAct = findViewById(R.id.iv_backAct_interestsAct);
        iv_submit = findViewById(R.id.iv_submit_interestsAct);

        user = (User) getIntent().getSerializableExtra("User");
        oppositeUsers = (ArrayList<User>) getIntent().getSerializableExtra("OppositeUsers");

        isRegisterInfo = getIntent().getBooleanExtra("isRegisterInfo", false);

        pd = new ProgressDialog(Activity_Interests.this);

        interestsAdapter = new InterestsAdapter(user.getInterests(), this, false);
        recyclerView = findViewById(R.id.recyclerView_interestsAct);
    }

    private void doFunctionInAct() {
        loadDataToRecyclerView();

        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(v -> callBackAct());

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(v -> callSubmitAct());
    }

    private void loadDataToRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(Activity_Interests.this, 3));
    }

    private void callBackAct() {
        if (isRegisterInfo) {
            Intent intent = new Intent(Activity_Interests.this, Activity_Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("User", user);
            intent.putExtra("OppositeUsers", oppositeUsers);
            intent.putExtra("MenuDefault", 0);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Activity_Interests.this, Activity_Type.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("User", user);
            startActivity(intent);
            finish();
        }
    }

    private void callSubmitAct() {

        if (isRegisterInfo) {
            pd.setMessage("Updating...");
            pd.show();
            String path = "Users/" + user.getInfo().getGender() + "/" + user.getLookingFor().getLooking() + "/" + user.getIdUser();
            FirebaseDatabase.getInstance().getReference(path).setValue(user)
                    .addOnSuccessListener(aVoid ->
                    {
                        pd.dismiss();
                        Toast.makeText(Activity_Interests.this, "Updating Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Interests.this, Activity_Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("User", user);
                        intent.putExtra("OppositeUsers", oppositeUsers);
                        intent.putExtra("MenuDefault", 0);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(Activity_Interests.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } else {
            Intent intent = new Intent(Activity_Interests.this, Activity_RegisterDone.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("User", user);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onInterestsClicked(int position, Boolean status) {
        user.getInterests().get(position).setStatus(status);
    }
}