package com.example.quickdate.activities_fragment.UI_QuickDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdate.R;
import com.example.quickdate.adapter.CardStackAdapter;
import com.example.quickdate.adapter.UserDiffCallBack;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Fragment_Swiper extends Fragment implements CardStackListener {


    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private CardStackAdapter cardStackAdapter;
    private ImageButton btn_skip, btn_love, btn_rewind;
    private User user;
    private ArrayList<User> myOppositeUsers;

    private ValueEventListener getAllOppositeUsersListener;
    private DatabaseReference refGetAllOppositeUsers;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swiper, container, false);
        init(view);
        doFunction();
        return view;
    }

    private void init(View view) {
        Activity_Home act = (Activity_Home) getActivity();
        assert act != null;
        myOppositeUsers = new ArrayList<>();
        user = act.getCurrentUser();

        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackLayoutManager = new CardStackLayoutManager(getActivity(), this);
        btn_skip = view.findViewById(R.id.skip_button);
        btn_love = view.findViewById(R.id.love_button);
        btn_rewind = view.findViewById(R.id.rewind_button);

        act.tv_head_title.setText("Quick Date");
    }

    private void doFunction() {
        setupCardStackView();
        setupButton();
    }

    private void setupButton() {
        PushDownAnim.setPushDownAnimTo(btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_rewind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                cardStackLayoutManager.setRewindAnimationSetting(setting);
                cardStackView.rewind();
            }
        });

        PushDownAnim.setPushDownAnimTo(btn_love).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
    }

    private void setupCardStackView() {
        cardStackLayoutManager.setStackFrom(StackFrom.None);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(8.0f);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setSwipeThreshold(0.3f);
        cardStackLayoutManager.setMaxDegree(20.0f);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(true);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackLayoutManager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView.setLayoutManager(cardStackLayoutManager);

        RecyclerView.ItemAnimator itemAnimator = cardStackView.getItemAnimator();
        if (itemAnimator instanceof DefaultItemAnimator) {
            DefaultItemAnimator di = (DefaultItemAnimator) itemAnimator;
            di.setSupportsChangeAnimations(false);
        }
    }



    private void paginate() {
        ArrayList<User> old = cardStackAdapter.getUsers();
        ArrayList<User> newer = old;
        newer.addAll(myOppositeUsers);
        UserDiffCallBack callback = new UserDiffCallBack(old, newer);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        cardStackAdapter.setUsers(newer);
        result.dispatchUpdatesTo(cardStackAdapter);
    }

    private void addToHisNotifications(String hisId) {
        String timeStamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("senderId", user.getIdUser());
        hashMap.put("receiverId", hisId);
        hashMap.put("type", "Liked");
        hashMap.put("notification", "Want to match with you");
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);

        FirebaseDatabase.getInstance().getReference("Notifications")
                .push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your liked was send", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<User> getOppositeUsers(){
        return  myOppositeUsers;
    }

    private void getAllOppositeUsers() {
        refGetAllOppositeUsers = FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender().equals("Male") ? "Female" : "Male");
        getAllOppositeUsersListener = refGetAllOppositeUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOppositeUsers.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User tempUser = ds.getValue(User.class);
                    if (tempUser.getInfo().getAge() >= user.getLookingFor().getMin_age() &&
                            tempUser.getInfo().getAge() <= user.getLookingFor().getMax_age() &&
                            tempUser.getLookingFor().getLooking().equals(user.getLookingFor().getLooking()) &&
                            tempUser.getInfo().getWeight() <= user.getLookingFor().getMax_weight() &&
                            tempUser.getInfo().getWeight() >= user.getLookingFor().getMin_weight() &&
                            tempUser.getInfo().getHeight() <= user.getLookingFor().getMax_height() &&
                            tempUser.getInfo().getHeight() >= user.getLookingFor().getMin_height())
                    {
                        FirebaseDatabase.getInstance().getReference("Users").child(user.getInfo().getGender()).child(user.getIdUser()).child("matchers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot ds : snapshot.getChildren()){
                                        if(!tempUser.getIdUser().equals(ds.getValue(String.class))){
                                            myOppositeUsers.add(tempUser);
                                        }
                                    }
                                }
                                else {
                                    myOppositeUsers.add(tempUser);
                                }
                                cardStackAdapter = new CardStackAdapter(getActivity() ,myOppositeUsers);
                                cardStackView.setAdapter(cardStackAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                int a =1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        getAllOppositeUsers();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        refGetAllOppositeUsers.removeEventListener(getAllOppositeUsersListener);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (cardStackLayoutManager.getTopPosition() == cardStackAdapter.getItemCount() - 5) {
            paginate();
        }
        if (direction == Direction.Right) {
            User test = myOppositeUsers.get(cardStackLayoutManager.getTopPosition() -1);
            addToHisNotifications(test.getIdUser());
        }else if(direction == Direction.Left){

        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
    }
}