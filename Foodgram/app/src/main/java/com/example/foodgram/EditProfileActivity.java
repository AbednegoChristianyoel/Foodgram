package com.example.foodgram;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.foodgram.Model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.StorageTask;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back , img_profile;
    TextView dp_change;
    Button save;
    TextInputEditText nama,bio, mail ;
    FirebaseUser firebaseUser;

    //    private Uri mImageUri;
//    private StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back = findViewById(R.id.back);
        img_profile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        dp_change = findViewById(R.id.dp_change);
        nama = findViewById(R.id.txtName);
        bio = findViewById(R.id.txtBio);
        mail = findViewById(R.id.txtEmail);
//        phone = findViewById(R.id.numPhone);
//        gender = findViewById(R.id.txtGender);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nama.setText(user.getNama());
                bio.setText(user.getBio());
                mail.setText(user.getEmail());
                //Phone
//                phone.setText(user.());
                //gender
//                gender.setText(user.getBio());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(img_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        dp_change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               CropImage.activity()
//                      .setAspectRatio(1);
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(nama.getText().toString(),
                        bio.getText().toString(),
                        mail.getText().toString());
                onBackPressed();

            }

        });

    }
    private void updateProfile(String name, String bio, String mail) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nama",name);
        hashMap.put("bio",bio);
        hashMap.put("email",mail);
        hashMap.put("search", name.toLowerCase());

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Update Succesfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Update gabisa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

//    private void UploadImg(){
//        ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Uploading");
//        pd.show();
//
//        if (mImageUri != null) {
//            StorageReference filereference = storageRef.child(System.currentTimeMillis()
//            +"."+ getFileExtension(mImageUri));
//
//            uploadTask = filereference.putFile(mImageUri);
//            uploadTask.continueWithTask(new Continuation() {
//                @Override
//                public Object then(@NonNull Task task) throws Exception {
//                    if (!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return  filereference.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()){
//                        Uri downloadUri = task.getResult();
//                        String myUrl = downloadUri.toString();
//
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//
//                        HashMap<String,Object> hashMap = new HashMap<>();
//                        hashMap.put("imageurl", ""+myUrl);
//
//                        reference.updateChildren(hashMap);
//                        pd.dismiss();
//                    } else {
//                        Toast.makeText(EditProfileActivity.this, "Failed",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }else {
//            Toast.makeText(this,"No image selected", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//            } else {
//                Toast.makeText(this, "something gone wrong", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
}