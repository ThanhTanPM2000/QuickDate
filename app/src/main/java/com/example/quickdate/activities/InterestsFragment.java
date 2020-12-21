package com.example.quickdate.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.InterestsAdapter;
import com.example.quickdate.listener.InterestsListener;
import com.example.quickdate.model.Interest;
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
    private ArrayList<Interest> interests;
    private ImageView iv_backAct, iv_submit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private View view;

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

        /*interests = new ArrayList<Interest>();
        interests.add(new Interest("Art & Design", false, R.drawable.image_artdesign));
        interests.add(new Interest("TV & Music", false, R.drawable.image_movie));
        interests.add(new Interest("Tech", false, R.drawable.image_tech));
        interests.add(new Interest("Food", false, R.drawable.image_food));
        interests.add(new Interest("Animals", false, R.drawable.image_animals));
        interests.add(new Interest("Fitness & Health", false, R.drawable.image_fitnessandhealth));
        interests.add(new Interest("Cars", false, R.drawable.image_cars));
        interests.add(new Interest("Sports", false, R.drawable.image_fooball));
        interests.add(new Interest("Books", false, R.drawable.image_book));*/

        interests = new ArrayList<Interest>();
        interests.add(new Interest("Art & Design", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("TV & Music", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Tech", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Food", false,"https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Animals", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Fitness & Health", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Cars", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Sports", false, "https://www.ruaviation.com/images/media/600/419.jpg"));
        interests.add(new Interest("Books", false, "https://www.ruaviation.com/images/media/600/419.jpg"));

        interestsAdapter = new InterestsAdapter(interests, this);
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
                //callBackAct();
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
        NavHostFragment.findNavController(InterestsFragment.this)
                .navigate(R.id.action_interestsFragment_to_typeFragment);
    }

    private void callSubmitAct(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/" + firebaseUser.getUid());
        databaseReference.child("interests").setValue(interests).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    NavHostFragment.findNavController(InterestsFragment.this)
                            .navigate(R.id.action_interestsFragment_to_doneFragment);

                }
                else{
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onInterestsClicked(int position, Boolean status) {
        interests.get(position).setStatus(status);
    }
}