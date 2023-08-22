package com.hammad.translator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hammad.translator.databinding.ActivityLogInBinding;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    ActivityLogInBinding binding;
    FirebaseAuth auth;
    String email,pass,forget;
    Boolean checkedittext=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        binding=ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        SharedPreferences sharedPreferences=getSharedPreferences("Checkbox", MODE_PRIVATE);
        String checkbox= sharedPreferences.getString("Remember","");
        if (checkbox.equals(true)){
            startActivity(new Intent(LogIn.this,MainActivity.class));
        } else if (checkbox.equals(false)){
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }




        auth=FirebaseAuth.getInstance();
        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=binding.loginemail.getText().toString().trim();
                pass=binding.loginpass.getText().toString();
                if(email.isEmpty()){
                    binding.loginemail.setError("Enter email first");
                         checkedittext=true;
                }if (pass.isEmpty()){
                    binding.loginpass.setError("Enter your password");
                    checkedittext=true;
                }else
                auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LogIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(LogIn.this,MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogIn.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });
        binding.forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forget=binding.loginemail.getText().toString().trim();
                String Email=binding.loginemail.getText().toString();
                String passward=binding.loginpass.getText().toString();

                auth.confirmPasswordReset(Email,passward).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LogIn.this, "check email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogIn.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    SharedPreferences sharedPreferences=getSharedPreferences("Checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Remember","True");
                    editor.apply();
                    Toast.makeText(LogIn.this, "Checked", Toast.LENGTH_SHORT).show();
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences sharedPreferences=getSharedPreferences("Checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Remember","False");
                    editor.apply();
                    Toast.makeText(LogIn.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=auth.getCurrentUser();
        if (currentuser!=null){
            startActivity(new Intent(LogIn.this,MainActivity.class));
            finish();
        }
    }

}

