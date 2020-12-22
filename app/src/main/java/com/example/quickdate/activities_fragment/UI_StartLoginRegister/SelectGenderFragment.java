package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.model.Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Objects;

public class SelectGenderFragment extends Fragment {

    private ConstraintLayout ctl_male, ctl_female;
    private ImageView iv_isCheckedFemale, iv_isCheckedMale, iv_submit;
    private FirebaseAuth firebaseAuth;
    private Info info;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_gender, container, false);
    }

    private void initialization(View view) {
        ctl_female = (ConstraintLayout) view.findViewById(R.id.ctl_chooseFemale_selectGenderAct);
        ctl_male = (ConstraintLayout) view.findViewById(R.id.ctl_chooseMale_selectGenderAct);
        iv_isCheckedFemale = (ImageView) view.findViewById(R.id.iv_isCheckedFemale_selectGenderAct);
        iv_isCheckedMale = (ImageView) view.findViewById(R.id.iv_isCheckedMale_selectGenderAct);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_selectGender);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Users/" + firebaseAuth.getCurrentUser().getUid() + "/info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                info = snapshot.getValue(Info.class);
                doFunctionInAct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doFunctionInAct() {
        PushDownAnim.setPushDownAnimTo(ctl_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(false);
            }
        });

        PushDownAnim.setPushDownAnimTo(ctl_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender(true);
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataFunction();
            }
        });
    }

    private void chooseGender(Boolean isMale) {
        if (isMale) {
            info.setMale(true);
            iv_isCheckedFemale.setVisibility(View.GONE);
            iv_isCheckedMale.setVisibility(View.VISIBLE);
        } else {
            info.setMale(false);
            iv_isCheckedFemale.setVisibility(View.VISIBLE);
            iv_isCheckedMale.setVisibility(View.GONE);
        }
    }

    private void submitDataFunction() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getCurrentUser().getUid());
        db.child("info").setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    NavHostFragment.findNavController(SelectGenderFragment.this)
                            .navigate(R.id.action_selectGenderFragment_to_bioPhotosFragment);
                } else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}