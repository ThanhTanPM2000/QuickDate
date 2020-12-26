package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.listener.InterestsListener;
import com.example.quickdate.model.Interest;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class InterestsFragment extends Fragment implements InterestsListener {
    private InterestsAdapter interestsAdapter;
    private RecyclerView recyclerView;
    private ImageView iv_backAct, iv_submit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private View view;
    private User user;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        doFunctionInAct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_interests, container, false);
        return view;
    }

    private void initialization(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_interestsAct);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_interestsAct);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_interestsAct);

        user = (User) getArguments().getSerializable("User");

        interestsAdapter = new InterestsAdapter(user.getInterests(), this, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_interestsAct);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void doFunctionInAct(){
        loadDataToRecyclerView();

        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackAct();
            }
        });

        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void loadDataToRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(interestsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    private void callBackAct(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        NavHostFragment.findNavController(InterestsFragment.this)
                .navigate(R.id.action_interestsFragment_to_typeFragment, bundle);
    }

    private void callSubmitAct(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user);
        NavHostFragment.findNavController(InterestsFragment.this)
                .navigate(R.id.action_interestsFragment_to_doneFragment, bundle);
    }

    @Override
    public void onInterestsClicked(int position, Boolean status) {
        user.getInterests().get(position).setStatus(status);
    }
}