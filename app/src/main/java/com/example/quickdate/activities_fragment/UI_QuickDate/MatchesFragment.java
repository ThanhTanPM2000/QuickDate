package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

    private EditText et_search;

    // Model
    private User myUser;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doFunction();
    }

    private void doFunction() {

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String query = et_search.getText().toString();
                    if(TextUtils.isEmpty(query)){
                        getAllMatcher();
                    }else{
                        searchUser(query.toLowerCase());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_matches, container, false);
        init(root);
        return root;
    }

    private void init(View root) {

        // Init view
        et_search = root.findViewById(R.id.et_search_matchers);

        // Init model
        SwipeAct act = (SwipeAct) getActivity();
        myUser = new User();
        myUser = act.getCurrentUser();
        act.tv_head_title.setText("Matches");

        // Init View
        recyclerView = root.findViewById(R.id.recyclerView_matchesFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        matcherArrayList = new ArrayList<>();

        getAllMatcher();

    }

    private void getAllMatcher() {
        // get all matcher user
        FirebaseDatabase.getInstance().getReference("Matcher").child(myUser.getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matcherArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
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

    private void searchUser(String query) {
        // get all matcher user
        FirebaseDatabase.getInstance().getReference("Matcher").child(myUser.getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matcherArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User tempUser = ds.getValue(User.class);
                    if(tempUser.getInfo().getNickname().toLowerCase().contains(query) || tempUser.getEmail().equals(query)){
                        matcherArrayList.add(tempUser);
                    }
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