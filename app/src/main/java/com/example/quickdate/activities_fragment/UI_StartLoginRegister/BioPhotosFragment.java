package com.example.quickdate.activities_fragment.UI_StartLoginRegister;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickdate.R;
import com.example.quickdate.adapter.ImageRegisterAdapter;
import com.example.quickdate.listener.ImagesListener;
import com.example.quickdate.model.User;
import com.example.quickdate.utility.UploadImage;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class BioPhotosFragment extends Fragment implements ImagesListener {

    // Components of View
    private ConstraintLayout contraintLayout_snakebar;
    private ArrayList<Uri> imagesURIForAdapter;
    private ArrayList<String> imagesURIForSave;
    private RecyclerView recyclerView;
    private ImageView iv_submit, iv_backAct;
    private ImageButton btn_uploadImage;
    private EditText et_nickName, et_aboutMe;
    private View root;
    private it.sephiroth.android.library.numberpicker.NumberPicker numberPicker_age, numberPicker_height, numberPicker_weight;

    private ProgressDialog pd;

    // Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private ArrayList<String> index;
    private User user;
    private UploadTask uploadTask;

    // Adapter
    ImageRegisterAdapter imageRegisterAdapter;

    // Path where images of user avatar will be stored
    String storagePath = "images";

    // Upload class
    UploadImage uploadImage;

    // Uri of Picked Image
    private Uri image_Uri;

    // Permissions Contants
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 200;
    public static final int IMAGE_PICK_GALLERY_CODE = 300;
    public static final int IMAGE_PICK_CAMERA_CODE = 400;

    // Array òf permissions to be requested
    private String[] cameraPermissions;
    private String[] storagePermissions;

    // check is first array image
    Boolean isFirstArrayImage = true;

    // avatarImg
    private String avatarImg;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view;
        init(root);
        doFunction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bio_photos, container, false);
    }

    private void init(View view) {

        // Init View
        btn_uploadImage = (ImageButton) view.findViewById(R.id.btn_uploadImage);
        et_nickName = (EditText) view.findViewById(R.id.et_nickName);
        et_aboutMe = (EditText) view.findViewById(R.id.et_aboutMe);
        numberPicker_age = view.findViewById(R.id.numberPicker_age);
        numberPicker_height = view.findViewById(R.id.numberPicker_height);
        numberPicker_weight = view.findViewById(R.id.numberPicker_weight);
        contraintLayout_snakebar = (ConstraintLayout) view.findViewById(R.id.contraintLayout_snakebar);
        iv_submit = (ImageView) view.findViewById(R.id.iv_submit_bioPhotosAct);
        iv_backAct = (ImageView) view.findViewById(R.id.iv_backAct_bioPhotosAct);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Init firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Init firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Init firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());

        // Init User variable from pass value between fragment
        user = (User) getArguments().getSerializable("User");

        // Init ProgressDialog
        pd = new ProgressDialog(getActivity());

        // Init array imagesUri
        imagesURIForAdapter = new ArrayList<>();
        imagesURIForSave = new ArrayList<String>();

        avatarImg = "";

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageRegisterAdapter = new ImageRegisterAdapter(imagesURIForAdapter, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        recyclerView.setAdapter(imageRegisterAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }

    private void doFunction() {
        PushDownAnim.setPushDownAnimTo(btn_uploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageFunction();
            }
        });

        // onClick when user click back
        PushDownAnim.setPushDownAnimTo(iv_backAct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackAct();
            }
        });

        // onClick when user submit
        PushDownAnim.setPushDownAnimTo(iv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSubmitAct();
            }
        });
    }

    private void uploadImageFunction() {
        // Show dialog containing options Camera and Gallary to pick the image

        String[] options = {"Camera", "Gallary"};

        // Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set title
        builder.setTitle("Upload Image From");
        // Set item
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle dialog item clicks
                pd.setMessage("Uploading...");
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
                        Toast.makeText(getActivity(), "Please enable camera & storage permission", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callBackAct() {
        if (NavHostFragment.findNavController(BioPhotosFragment.this).getCurrentDestination().getId() == R.id.bioPhotosFragment) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("User", user);
            NavHostFragment.findNavController(BioPhotosFragment.this)
                    .navigate(R.id.action_bioPhotosFragment_to_selectGenderFragment, bundle);
        }
    }

    private void callSubmitAct() {
        if (checkDataInput(et_nickName.getText().toString(), et_aboutMe.getText().toString())) {
            //index = new ArrayList<>(imagesURI.keySet());
            //guestImage = imagesURI.get(index.get(0));
            pd.show();
            for (Uri item : imagesURIForAdapter) {
                uploadAvatarImage(item);
            }
        }
    }

    private boolean checkDataInput(String nickName, String aboutMe) {
        {
            if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(aboutMe)) {
                Snackbar.make(contraintLayout_snakebar, "All fields should not empty", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (nickName.length() < 5) {
                Snackbar.make(contraintLayout_snakebar, "Field Nickname at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (aboutMe.length() <= 5) {
                Snackbar.make(contraintLayout_snakebar, "Field about me at least than 5 character", BaseTransientBottomBar.LENGTH_LONG).show();
            } else if (imagesURIForAdapter.size() < 3 || imagesURIForAdapter.size() > 5) {
                Snackbar.make(contraintLayout_snakebar, "You only Upload between 3 to 5 Images", BaseTransientBottomBar.LENGTH_LONG).show();
            } else {
                return true;
            }
            return false;
        }
    }

    @Override
    public void onImageClicked(int position) {
        imagesURIForAdapter.remove(position);
        imageRegisterAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // This method will be called after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            assert data != null : "Data null";
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // Image is picked from gallery, get uri of image
                image_Uri = data.getData();
                imagesURIForAdapter.add(image_Uri);
                Log.d("Test Add", imagesURIForAdapter.get(0).toString());
                imageRegisterAdapter.notifyItemInserted(imagesURIForAdapter.size());
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // Image is picked from camera, get uri of image
                imagesURIForAdapter.add(image_Uri);
                imageRegisterAdapter.notifyItemInserted(imagesURIForAdapter.size());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadAvatarImage(Uri uri) {
        // Path and name of Image to be stored in firebase storage
        String filePathAndName;
        if (isFirstArrayImage) {
            filePathAndName = storagePath + "/" + user.getIdUser() + "/avatar/" + System.currentTimeMillis();
            isFirstArrayImage = false;
        } else {
            filePathAndName = storagePath + "/" + user.getIdUser() + "/cover/" + System.currentTimeMillis();
        }

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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

                            if (isFirstArrayImage) {
                                avatarImg = downloadUri.toString();
                            }

                            if (NavHostFragment.findNavController(BioPhotosFragment.this).getCurrentDestination().getId() == R.id.bioPhotosFragment
                                    && imagesURIForAdapter.indexOf(uri) == imagesURIForAdapter.size() -1) {
                                user.getInfo().setImgAvt(avatarImg);
                                user.getInfo().setImages(imagesURIForSave);
                                user.getInfo().setNickname(et_nickName.getText().toString());
                                user.getInfo().setAboutMe(et_aboutMe.getText().toString());
                                user.getInfo().setAge(numberPicker_age.getProgress());
                                user.getInfo().setHeight(numberPicker_height.getProgress());
                                user.getInfo().setWeight(numberPicker_weight.getProgress());

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("User", user);
                                NavHostFragment.findNavController(BioPhotosFragment.this)
                                        .navigate(R.id.action_bioPhotosFragment_to_typeFragment, bundle);
                                pd.dismiss();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There were some error(s), get and show error message, dismiss progress dialog
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkStoragePermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result;
    }

    private void requestStoragePermission() {
        // Request runtime storage permission
        getActivity().requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        // Check if storage permission is enable or not
        // return true if enabled
        // return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return !result || !result1;
    }

    private void requestCameraPermission() {
        // Request runtime storage permission
        getActivity().requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        // Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // Put image Uri
        image_Uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

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