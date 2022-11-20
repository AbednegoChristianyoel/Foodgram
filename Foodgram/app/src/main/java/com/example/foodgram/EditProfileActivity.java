package com.example.foodgram;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import com.example.foodgram.Fragment.BottomNav;
import com.example.foodgram.Model.User;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;
//import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back , img_profile;
    TextView dp_change, numphone;
    Button save;
    TextInputEditText nama,bio, mail ;
    FirebaseUser firebaseUser;

    private StorageTask uploadTask;
    StorageReference storageRef;
    String destination = UUID.randomUUID().toString() + ".jpg";
    private Uri mImageUri;
    String myUrl = "";

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
        numphone = findViewById(R.id.numPhone);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nama.setText(user.getNama());
                bio.setText(user.getBio());
                mail.setText(user.getEmail());
                numphone.setText(user.getNophone());
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(nama.getText().toString(),
                        bio.getText().toString(),
                        mail.getText().toString());
                finish();

            }

        });

        storageRef = FirebaseStorage.getInstance().getReference("PhotoProfile");

        dp_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage(){
        final CharSequence[] items = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setItems(items, (dialog, item) ->{
            if(items[item].equals("Choose from Gallery")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 20);
            }else if(items[item].equals("Cancel")){
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20 && resultCode==RESULT_OK && data !=null){
            final Uri path = data.getData();

            UCrop.Options options = new UCrop.Options();
            options.setCircleDimmedLayer(true);
            UCrop.of(path, Uri.fromFile(new File(getCacheDir(), destination)))
                    .withAspectRatio(1,1)
                    .withOptions(options)
                    .start(this);

        }else if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            Uri resulturi = UCrop.getOutput(data);
            mImageUri = resulturi;
            img_profile.setImageURI(mImageUri);
            img_profile.setBackgroundResource(0);
            upload_image();
        }else{
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            finish();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upload_image(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (mImageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageurl", myUrl);


                        reference.updateChildren(hashMap);

                        pd.dismiss();

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
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
}