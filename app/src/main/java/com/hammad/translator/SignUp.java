package com.hammad.translator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hammad.translator.databinding.ActivitySignUpBinding;


public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference refrance;
   ProgressDialog dia;
    String fullname, pass,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        dia=new ProgressDialog(this);
        db=FirebaseDatabase.getInstance();
        binding.signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.show();
                email=binding.signemail.getText().toString().trim();
                pass=binding.signpass.getText().toString();
                fullname=binding.fullname.getText().toString();
                phone=binding.phone.getText().toString();
                auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dia.cancel();
                        if(!fullname.isEmpty()&&!email.isEmpty()&&!pass.isEmpty()&&!phone.isEmpty()){
                            userdata user=new userdata(fullname,pass,email,phone);
                            refrance=db.getReference("User");
                            refrance.child(fullname).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.fullname.setText("");
                                    binding.signemail.setText("");
                                    binding.signpass.setText("");
                                    binding.phone.setText("");
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });


            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,LogIn.class));
            }
        });
    }
}