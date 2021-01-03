package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.adapter.MatcherAdapter;
import com.example.quickdate.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_Matches extends Fragment {

    RecyclerView recyclerView;
    MatcherAdapter matcherAdapter;

    ArrayList<String> idMatcherList;
    ArrayList<User> matcherArrayList;

    private EditText et_search;

    private ValueEventListener checkOnlineMatcherListener;
    private DatabaseReference refCheckOnlineMatcher;

    private ValueEventListener getMatchersListener;
    private DatabaseReference refGetMatcher;

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
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String query = et_search.getText().toString();
                    if (TextUtils.isEmpty(query)) {
                        findIdMatchers();
                    } else {
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
        Activity_Home act = (Activity_Home) getActivity();
        myUser = new User();
        myUser = act.getCurrentUser();
        act.tv_head_title.setText("Matches");

        // Init View
        recyclerView = root.findViewById(R.id.recyclerView_matchesFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        idMatcherList = new ArrayList<>();
        matcherArrayList = new ArrayList<>();
    }

    private void searchUser(String query) {
        // get all matcher user
        FirebaseDatabase.getInstance().getReference("Users").child(myUser.getInfo().getGender()).child(myUser.getIdUser()).child("matchers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matcherArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User tempUser = ds.getValue(User.class);
                    if (tempUser.getInfo().getNickname().toLowerCase().contains(query) || tempUser.getEmail().equals(query)) {
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

    @Override
    public void onPause() {
        super.onPause();
        refGetMatcher.removeEventListener(getMatchersListener);
    }

    @Override
    public void onStart() {
        findIdMatchers();
        super.onStart();
    }

    private void findIdMatchers() {
        refGetMatcher = FirebaseDatabase.getInstance().getReference("Users").child(myUser.getInfo().getGender()).child(myUser.getIdUser()).child("matchers");
        getMatchersListener = refGetMatcher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idMatcherList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    idMatcherList.add(ds.getValue(String.class));
                }
                findAllMatchers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findAllMatchers() {
        FirebaseDatabase.getInstance().getReference("Users").child(myUser.getInfo().getGender().equals("Male") ? "Female" : "Male").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matcherArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (idMatcherList.contains(user.getIdUser())) {
                        matcherArrayList.add(user);
                    }
                    matcherAdapter = new MatcherAdapter(getActivity(), matcherArrayList, myUser);
                    recyclerView.setAdapter(matcherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}