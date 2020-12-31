package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.adapter.MatcherAdapter;
import com.example.quickdate.model.Matcher;
import com.example.quickdate.model.OppositeUsers;
import com.example.quickdate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MatchesFragment extends Fragment {

    RecyclerView recyclerView;
    MatcherAdapter matcherAdapter;
    ArrayList<User> matcherArrayList;

    // Model
    private User myUser;
    private OppositeUsers oppositeUsers;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_matches, container, false);
        init(root);
        return root;
    }

    private void init(View root) {

        // Init model
        SwipeAct act = (SwipeAct) getActivity();
        myUser = new User();
        oppositeUsers = new OppositeUsers();
        myUser = act.getCurrentUser();
        oppositeUsers = act.getAllOppositeUsers();
        act.tv_head_title.setText("Matches");

        // Init View
        recyclerView = root.findViewById(R.id.recyclerView_matchesFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        matcherArrayList = new ArrayList<>();

        getAllMatcher();

    }

    private void getAllMatcher() {
        // get current user
        FirebaseDatabase.getInstance().getReference("Matcher").child(myUser.getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matcherArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    matcherArrayList.add(ds.getValue(User.class));

                    matcherAdapter = new MatcherAdapter(getActivity(), matcherArrayList);
                    recyclerView.setAdapter(matcherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}