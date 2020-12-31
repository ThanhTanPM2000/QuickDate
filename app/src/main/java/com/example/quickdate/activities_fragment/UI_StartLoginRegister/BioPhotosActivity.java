package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.activities_fragment.UI_QuickDate.SwipeAct;
import com.example.quickdate.adapter.ImageRegisterAdapter;
import com.example.quickdate.listener.ImagesListener;
import com.example.quickdate.model.User;
import com.example.quickdate.model.OppositeUsers;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class BioPhotosActivity extends AppCompatActivity implements ImagesListener {

    // Components of View
    private ConstraintLayout constraintsLayout_snake;
    private ArrayList<String> imagesURIForAdapter;
    private ArrayList<String> imagesURIForSave;
    private ImageView iv_submit, iv_backAct;
    private ImageButton btn_uploadImage;
    private EditText et_nickName, et_aboutMe;
    private it.sephiroth.android.library.numberpicker.NumberPicker numberPicker_age, numberPicker_height, numberPicker_weight;
    private RecyclerView recyclerView;
    private ProgressDialog pd;

    private StorageReference storageReference;

    // Model
    private User user;
    private OppositeUsers oppositeUsers;

    // Adapter
    private ImageRegisterAdapter imageRegisterAdapter;

    // Path where images of user avatar will be stored
    private String storagePath = "images";

    // Uri of Picked Image
    private Uri image_Uri;

    // Permissions Constants
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 200;
    public static final int IMAGE_PICK_GALLERY_CODE = 300;
    public static final int IMAGE_PICK_CAMERA_CODE = 400;

    // Array òf permissions to be requested
    private String[] cameraPermissions;
    private String[] storagePermissions;

    // check is first array image
    private Boolean isFailed = false;

    // is Register Info
    private Boolean isRegisterInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_photos);
        init();
        doFunction();
    }

    private void init() {

        // Init View
        btn_uploadImage = findViewById(R.id.btn_uploadImage);
        et_nickName = findViewById(R.id.et_nickName);
        et_aboutMe = findViewById(R.id.et_aboutMe);
        numberPicker_age = findViewById(R.id.numberPicker_age);
        numberPicker_height = findViewById(R.id.numberPicker_height);
        numberPicker_weight = findViewById(R.id.numberPicker_weight);
        constraintsLayout_snake = findViewById(R.id.contraintLayout_snakebar);
        iv_submit = findViewById(R.id.iv_submit_bioPhotosAct);
        iv_backAct = findViewById(R.id.iv_backAct_bioPhotosAct);
        recyclerView = findViewById(R.id.recyclerView);

        // Init firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Init User variable from pass value between fragment
        user = (User) getIntent().getSerializableExtra("User");
        oppositeUsers = (OppositeUsers) getIntent().getSerializableExtra("OppositeUsers");

        // Init Data default
        et_nickName.setText(user.getInfo().getNickname());
        et_aboutMe.setText(user.getInfo().getAboutMe());

        isRegisterInfo = getIntent().getBooleanExtra("isRegisterInfo", false);

        if (user.getInfo().getImages() != null) {
            imagesURIForAdapter = user.getInfo().getImages();
        } else {
            imagesURIForAdapter = new ArrayList<>();
        }
        imagesURIForSave = new ArrayList<>(imagesURIForAdapter);

        // Init ProgressDialog
        pd = new ProgressDialog(BioPhotosActivity.this);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        loadImagesToRecycleView();
    }

    private void loadImagesToRecycleView() {
        imageRegisterAdapter = new ImageRegisterAdapter(imagesURIForAdapter, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(BioPhotosActivity.this, RecyclerView.HORIZONTAL, false);

        recyclerView.setAdapter(imageRegisterAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }

    private void doFunction() {
        PushDownAnim.setPushDownAnimTo(btn_uploadImage).setOnClickListener(v -> uploadImageFunction());

        // onClick when user click back
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(v -> callBackAct());

        // onClick when user submit
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(v -> callSubmitAct());
    }

    private void uploadImageFunction() {
        // Show dialog containing options Camera and Gallary to pick the image

        String[] options = {"Camera", "Gallary"};

        // Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(BioPhotosActivity.this);
        // Set title
        builder.setTitle("Upload Image From");
        // Set item
        builder.setItems(options, (dialog, which) -> {
            // Handle dialog item clicks
            if (isRegisterInfo) {
                pd.setMessage("Updating...");
            } else {
                pd.setMessage("Uploading...");
            }

            if (which == 0) {
                // Camera click
                if (checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromCamera();
                }
            } else if (which == 1) {
                // Gallery click
                if (checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                // Picking from camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccept && writeStorageAccepted) {
                        // Permissions allowed
                        pickFromCamera();
                    } else {
                        // Permissions denied
                        Toast.makeText(BioPhotosActivity.this, "Please enable camera & storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                // Picking from gallery, first check if storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        // Permissions allowed
                        pickFromGallery();
                    } else {
                        // Permissions denied
                        Toast.makeText(BioPhotosActivity.this, "Please enable storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callBackAct() {
        if (isRegisterInfo) {
            Intent intent = new Intent(BioPhotosActivity.this, SwipeAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("OppositeUsers", oppositeUsers);
            intent.putExtra("MenuDefault", 0);
            intent.putExtra("User", user);
            startActivity(intent);
            finish();
            pd.dismiss();
        } else {
            Intent intent = new Intent(BioPhotosActivity.this, SelectGenderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void callSubmitAct() {
        if (checkDataInput(et_nickName.getText().toString(), et_aboutMe.getText().toString())) {
            //index = new ArrayList<>(imagesURI.keySet());
            //guestImage = imagesURI.get(index.get(0));
            pd.show();
            for (String item : imagesURIForAdapter) {
                    Uri uri = Uri.parse(item);
                    uploadAvatarImage(uri);
            }
        }
    }

    private boolean checkDataInput(String nickName, String aboutMe) {
        {
            if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(aboutMe)) {
                Snackbar.make(constraintsLayout_snake, "All fields should not empty", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (nickName.length() < 5) {
                Snackbar.make(constraintsLayout_snake, "Field Nickname at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (aboutMe.length() <= 5) {
                Snackbar.make(constraintsLayout_snake, "Field about me at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (imagesURIForAdapter.size() < 3 || imagesURIForAdapter.size() > 5) {
                Snackbar.make(constraintsLayout_snake, "You only Upload between 3 to 5 Images", BaseTransientBottomBar.LENGTH_LONG).show();
            } else {
                return true;
            }
            return false;
        }
    }

    @Override
    public void onImageClicked(int position) {
        if(position < imagesURIForSave.size()){
            imagesURIForSave.remove(position);
        }
        imagesURIForAdapter.remove(position);
        loadImagesToRecycleView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // This method will be called after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            assert data != null : "Data null";
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // Image is picked from gallery, get uri of image
                image_Uri = data.getData();
                imagesURIForAdapter.add(image_Uri.toString());
                loadImagesToRecycleView();
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // Image is picked from camera, get uri of image
                imagesURIForAdapter.add(image_Uri.toString());
                loadImagesToRecycleView();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadAvatarImage(Uri uri) {
        // Path and name of Image to be stored in firebase storage
        String filePathAndName = storagePath + "/" + user.getIdUser() + "/cover/" + System.currentTimeMillis();

        user.getInfo().setImages(imagesURIForSave);
        user.getInfo().setImgAvt(user.getInfo().getImgAvt());
        user.getInfo().setNickname(et_nickName.getText().toString());
        user.getInfo().setAboutMe(et_aboutMe.getText().toString());
        user.getInfo().setAge(numberPicker_age.getProgress());
        user.getInfo().setHeight(numberPicker_height.getProgress());
        user.getInfo().setWeight(numberPicker_weight.getProgress());

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image is uploaded to storage, now get it's url and store in user's database
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadUri = uriTask.getResult();
                    // Check if image is uploaded or not and url is received
                    if (uriTask.isSuccessful()) {
                        // Image uploaded
                        // add/upload url in User's database
                        assert downloadUri != null;
                        imagesURIForSave.add(downloadUri.toString());

                        if (imagesURIForAdapter.indexOf(uri.toString()) == imagesURIForAdapter.size() - 1) {
                            user.getInfo().setImages(imagesURIForSave);

                            if (isRegisterInfo) {
                                String path = "Users/" + user.getInfo().getGender() + "/" + user.getLookingFor().getLooking() + "/" + user.getIdUser();
                                FirebaseDatabase.getInstance().getReference(path).setValue(user)
                                        .addOnSuccessListener(aVoid ->
                                        {
                                            Toast.makeText(BioPhotosActivity.this, "Updating Successfully", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                            Intent intent = new Intent(BioPhotosActivity.this, SwipeAct.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("OppositeUsers", oppositeUsers);
                                            intent.putExtra("MenuDefault", 0);
                                            intent.putExtra("User", user);
                                            startActivity(intent);
                                            finish();
                                        }).addOnFailureListener(e ->
                                        Toast.makeText(BioPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                            } else {

                                    pd.dismiss();
                                    user.getInfo().setImgAvt(downloadUri.toString());
                                    Intent intent = new Intent(BioPhotosActivity.this, TypeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("User", user);
                                    startActivity(intent);
                                    finish();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                        // There were some error(s), get and show error message, dismiss progress dialog
                    if (imagesURIForAdapter.indexOf(uri.toString()) == imagesURIForAdapter.size() - 1) {
                        String path = "Users/" + user.getInfo().getGender() + "/" + user.getLookingFor().getLooking() + "/" + user.getIdUser();
                        FirebaseDatabase.getInstance().getReference(path).setValue(user)
                                .addOnSuccessListener(aVoid ->
                                {
                                    Intent intent = new Intent(BioPhotosActivity.this, SwipeAct.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("MenuDefault", 0);
                                    intent.putExtra("User", user);
                                    intent.putExtra("OppositeUsers", oppositeUsers);
                                    startActivity(intent);
                                    finish();
                                    pd.dismiss();
                                }).addOnFailureListener(y ->
                                Toast.makeText(BioPhotosActivity.this, y.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private boolean checkStoragePermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(BioPhotosActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result;
    }

    private void requestStoragePermission() {
        // Request runtime storage permission
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(BioPhotosActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(BioPhotosActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result || !result1;
    }

    private void requestCameraPermission() {
        // Request runtime storage permission
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        // Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // Put image Uri
        image_Uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        // Pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }
}