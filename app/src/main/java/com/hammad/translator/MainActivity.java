package com.hammad.translator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;

import com.airbnb.lottie.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hammad.translator.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mbinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.black));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mbinding.getRoot());

        mbinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

        mbinding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });




        mbinding.cardtranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LanguageTrans.class));
            }
        });
        mbinding.cardspeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VoiceToText.class));
            }
        });

    }
}