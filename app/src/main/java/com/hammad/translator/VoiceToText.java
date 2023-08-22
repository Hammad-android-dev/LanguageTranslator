package com.hammad.translator;

import static com.hammad.translator.LanguageTrans.REQUEST_PERMISSION_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;
public class VoiceToText extends AppCompatActivity {
    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdit;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    String[] fromlanguages = {"From", "ENGLISH", "BENGALI", "HINDI", "PORTUGUESE","RUSSIAN","JAPANESE","GERMAN, STANDARD","CHINESE WU","JAVANESE","KOREAN","FRENCH","URDU","TURKISH","ARABIC","UKRAINIAN","ITALIAN","SOMALI","BULGARIAN", };

    String [] tolanguage = {"TO", "ENGLISH", "BENGALI", "HINDI", "PORTUGUESE","RUSSIAN","JAPANESE","GERMAN, STANDARD","CHINESE WU","JAVANESE","KOREAN","FRENCH","URDU","TURKISH","ARABIC","UKRAINIAN","ITALIAN","SOMALI","BULGARIAN", };

    private static  final int REQUEST_PERMISSION_CODE = 1;
    int languageCode,fromLanguageCode, tolanguageCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_to_text);
        fromSpinner = findViewById(R.id.fromspinner2);
        toSpinner = findViewById(R.id.tospiner2);
        sourceEdit = findViewById(R.id.editsourcetext2);
        micIV = findViewById(R.id.mic);
        translateBtn = findViewById(R.id.btntranslate2);
        translatedTV = findViewById(R.id.translatedtext2);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode =getLanguageCode(fromlanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinneritem, fromlanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fromSpinner.setAdapter(fromAdapter);
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tolanguageCode = getLanguageCode(tolanguage[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinneritem,tolanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        toSpinner.setAdapter(toAdapter);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if (sourceEdit.getText().toString().isEmpty()){
                    Toast.makeText(VoiceToText.this, "Please Enter your translate", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode==0) {
                    Toast.makeText(VoiceToText.this, "Please Select source language", Toast.LENGTH_SHORT).show();
                } else if (tolanguageCode==0) {
                    Toast.makeText(VoiceToText.this, "Please select the language to translate", Toast.LENGTH_SHORT).show();
                }else {
                    translateText(fromLanguageCode,tolanguageCode,sourceEdit.getText().toString());

                }
            }
        });
        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text");
                try {
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(VoiceToText.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PERMISSION_CODE){
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdit.setText(result.get(0));
            }
        }
    }

    private void translateText(int fromLanguageCode, int tolanguageCode, String source){
        translatedTV.setText("Downloding Model...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(tolanguageCode).build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translatedTV.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VoiceToText.this, "Fail to translate : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VoiceToText.this, "Fial to download Model "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public int getLanguageCode(String language ) {
        int languageCode = 0;
        switch (language){
            case "ENGLISH":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "BENGALI":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "HINDI":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "PORTUGUESE":
                languageCode = FirebaseTranslateLanguage.PT;
                break;
            case "RUSSIAN":
                languageCode = FirebaseTranslateLanguage.RU;
                break;
            case "JAPANESE":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            case "GERMAN":
                languageCode = FirebaseTranslateLanguage.GA;
                break;
            case "CHINESE WU":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "KOREAN":
                languageCode = FirebaseTranslateLanguage.KO;
                break;
            case "FRENCH":
                languageCode = FirebaseTranslateLanguage.FR;
                break;
            case "URDU":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            case "TURKISH":
                languageCode = FirebaseTranslateLanguage.TR;
                break;

            case "ARABIC":
                languageCode = FirebaseTranslateLanguage.AR;
                break;

            case "UKRAINIAN":
                languageCode = FirebaseTranslateLanguage.UR;
                break;

            case "ITALIAN":
                languageCode = FirebaseTranslateLanguage.IT;
                break;

            case "SOMALI":
                languageCode = FirebaseTranslateLanguage.SL;
                break;

            case "BULGARIAN":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            default:
                languageCode =0;



        }
        return languageCode;
    }
}
