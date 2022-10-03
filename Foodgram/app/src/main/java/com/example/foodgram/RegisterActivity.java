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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
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
                Register();
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

    private void Register(){
        String nama = name.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String num = pnumber.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String umur = age.getText().toString().trim();

        if(nama.isEmpty()){
            name.setError("Nama Tidak boleh kosong!");
        }else if(mail.isEmpty()){
            email.setError("Email Tidak boleh kosong!");
        }else if(num.isEmpty()){
            pnumber.setError("Nomor Telepon Tidak boleh kosong!");
        }else if(pass.isEmpty()){
            password.setError("Password Tidak boleh kosong!");
        }else if(umur.isEmpty()){
            age.setError("Umur Tidak boleh kosong!");
        }
        else{
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegisterActivity.this,BottomNav.class));
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}