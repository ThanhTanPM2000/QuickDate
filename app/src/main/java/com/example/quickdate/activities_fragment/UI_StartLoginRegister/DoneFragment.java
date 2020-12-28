package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.SwipeAct;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Objects;

public class DoneFragment extends Fragment {

    private ImageView iv_submit;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private View view;
    private ProgressBar progressBar;
    private User user;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
        this.view = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_done, container, false);
    }

    private void initialization(View view) {
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_doneAct);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_done);

        user = (User) getArguments().getSerializable("User");
    }

    private void doFunctionInAct() {
        user.setStatus(1);
        databaseReference = firebaseDatabase.getReference("Users/" + user.getInfo().getGender() + "/" + user.getLookingFor().getLooking() + "/" + firebaseUser.getUid());
        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    iv_submit.setVisibility(View.VISIBLE);

                    databaseReference = firebaseDatabase.getReference("Users/UnRegisters/" + firebaseUser.getUid());
                    databaseReference.removeValue();
                } else {
                    Snackbar.make(view, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void callSubmitAct() {
        Intent intent = new Intent(getActivity(), SwipeAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
        return;
    }
}