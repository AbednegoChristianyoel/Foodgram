package com.example.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodgram.Fragment.BottomNav;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference reference;

    private EditText name, email, password, pnumber, age;
    private Button btn_Regis;
    private TextView textRegis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        name  = findViewById(R.id.namereg);
        email = findViewById(R.id.emailreg);
        password = findViewById(R.id.passwordreg);
        pnumber = findViewById(R.id.phonereg);
        age = findViewById(R.id.agereg);
        btn_Regis = findViewById(R.id.btn_regis);
        textRegis = findViewById(R.id.textRegis);

        btn_Regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_nama = name.getText().toString().trim();
                String str_mail = email.getText().toString().trim();
                String str_num = pnumber.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                String str_umur = age.getText().toString().trim();

                if(str_nama.isEmpty()){
                    name.setError("Nama Tidak boleh kosong!");
                }else if(str_mail.isEmpty()){
                    email.setError("Email Tidak boleh kosong!");
                }else if(str_num.isEmpty()){
                    pnumber.setError("Nomor Telepon Tidak boleh kosong!");
                }else if(str_pass.isEmpty()){
                    password.setError("Password Tidak boleh kosong!");
                }else if(str_umur.isEmpty()){
                    age.setError("Umur Tidak boleh kosong!");
                }else{
                    Register(str_nama, str_pass, str_mail, str_num, str_umur);
                }
            }
        });

        textRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Register(String nama, String password, String email, String nphone, String age){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("nama", nama);
                        hashMap.put("email", email);
                        hashMap.put("nophone", nphone);
                        hashMap.put("age", age);
                        hashMap.put("search", nama.toLowerCase());
                        hashMap.put("bio", "");
                        hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/foodgram-6b3b2.appspot.com/o/profile.jpg?alt=media&token=336daeae-78d2-44e3-88e8-d5f764512ce9");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(RegisterActivity.this, BottomNav.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }