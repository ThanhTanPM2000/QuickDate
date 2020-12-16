package com.example.quickdate.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.ImageRegisterAdapter;
import com.example.quickdate.listener.ImagesListener;
import com.example.quickdate.model.Info;
import com.example.quickdate.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BioPhotosAct extends AppCompatActivity implements ImagesListener {

    private ConstraintLayout contraintLayout_snakebar;
    private final HashMap<String, String> imagesURI = new HashMap<>();

    private RecyclerView recyclerView;
    private String localFileUri;
    private ImageView iv_uploadImage, iv_submit, iv_backAct;
    private EditText et_nickName, et_aboutMe;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ArrayList<String> index;
    private Info info;
    private UploadTask uploadTask;
    private it.sephiroth.android.library.numberpicker.NumberPicker numberPicker_age, numberPicker_height, numberPicker_weight;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            localFileUri = data.getData().getLastPathSegment();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            uploadTask = storageReference.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + data.getData().getLastPathSegment()).putFile(data.getData(), metadata);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Snackbar.make(contraintLayout_snakebar, "Upload " + progress + "%", 5000).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadTask.cancel();
                        }
                    }).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(contraintLayout_snakebar, "Failed to upload", BaseTransientBottomBar.LENGTH_LONG).show();
                    Snackbar.make(contraintLayout_snakebar, "Failed", 2000).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saveDataToRealTimeDatabaseFunc();
                    Snackbar.make(contraintLayout_snakebar, "Successfully", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Snackbar.make(contraintLayout_snakebar, "Paused", 2000).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_photos);
        initialization();
        doFunctionInAct();
    }

    private void initialization() {
        iv_uploadImage = (ImageView) findViewById(R.id.iv_uploadImage);
        et_nickName = (EditText) findViewById(R.id.et_nickName);
        et_aboutMe = (EditText) findViewById(R.id.et_aboutMe);
        numberPicker_age = findViewById(R.id.numberPicker_age);
        numberPicker_height = findViewById(R.id.numberPicker_height);
        numberPicker_weight = findViewById(R.id.numberPicker_weight);
        contraintLayout_snakebar = (ConstraintLayout) findViewById(R.id.contraintLayout_snakebar);
        iv_submit = (ImageView) findViewById(R.id.iv_submit_bioPhotosAct);
        iv_backAct = (ImageView) findViewById(R.id.iv_backAct_bioPhotosAct);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());
    }

    private void doFunctionInAct() {
        uploadImageFunction();
        callBackAct();
        callSubmitAct();
    }

    private void uploadImageFunction() {
        PushDownAnim.setPushDownAnimTo(iv_uploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void callBackAct() {
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelectGenderAct.class));
                finish();
            }
        });
    }

    private void callSubmitAct() {
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataInput(et_nickName.getText().toString(), et_aboutMe.getText().toString())) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            assert user != null : "user not found";
                            String guestImage;
                            index = new ArrayList<>(imagesURI.keySet());
                            if (imagesURI == null) {
                                guestImage = user.getInfo().getImgAvt();
                            } else {
                                guestImage = imagesURI.get(index.get(0));
                            }
                            info = new Info(imagesURI, guestImage, et_nickName.getText().toString(), et_aboutMe.getText().toString(), user.getInfo().isMale(), numberPicker_age.getProgress(), numberPicker_height.getProgress(), numberPicker_weight.getProgress());
                            db.child("info").setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), TypeAct.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void saveDataToRealTimeDatabaseFunc() {
        StorageReference ref = firebaseStorage.getReference("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + localFileUri);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (imagesURI.get(ref.getName()) == null) {
                            imagesURI.put(ref.getName(), uri.toString());
                            addDataToRecyclerViewFunc();
                        } else {
                            Snackbar.make(contraintLayout_snakebar, "This Image has already Upload", BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private void addDataToRecyclerViewFunc() {
        ImageRegisterAdapter imageRegisterAdapter = new ImageRegisterAdapter(imagesURI, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        recyclerView.setAdapter(imageRegisterAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }

    private boolean checkDataInput(String nickName, String aboutMe) {
        {
            if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(aboutMe)) {
                Snackbar.make(contraintLayout_snakebar, "All fields should not empty", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (nickName.length() < 5) {
                Snackbar.make(contraintLayout_snakebar, "Field Nickname at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (aboutMe.length() <= 5) {
                Snackbar.make(contraintLayout_snakebar, "Field about me at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (imagesURI.size() < 3 || imagesURI.size() > 5) {
                Snackbar.make(contraintLayout_snakebar, "You only Upload between 3 to 5 Images", BaseTransientBottomBar.LENGTH_LONG).show();
            } else {
                return true;
            }
            return false;
        }
    }

    @Override
    public void onImageClicked(String imageKey) {
        imagesURI.remove(imageKey);
        addDataToRecyclerViewFunc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}