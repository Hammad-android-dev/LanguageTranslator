package com.hammad.translator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.hammad.translator.databinding.ActivityLanguageTransBinding;

import java.util.ArrayList;
import java.util.Locale;

public class LanguageTrans extends AppCompatActivity {
    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdit;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    String[] fromlanguages = {"From", "ENGLISH", "BENGALI", "HINDI", "PORTUGUESE", "RUSSIAN", "JAPANESE", "GERMAN, STANDARD", "CHINESE WU", "JAVANESE", "KOREAN", "FRENCH", "URDU", "TURKISH", "ARABIC", "UKRAINIAN", "ITALIAN", "SOMALI", "BULGARIAN",};

    String[] tolanguage = {"TO", "ENGLISH", "BENGALI", "HINDI", "PORTUGUESE", "RUSSIAN", "JAPANESE", "GERMAN, STANDARD", "CHINESE WU", "JAVANESE", "KOREAN", "FRENCH", "URDU", "TURKISH", "ARABIC", "UKRAINIAN", "ITALIAN", "SOMALI", "BULGARIAN",};

    static final int REQUEST_PERMISSION_CODE = 1;
    int languageCode, fromLanguageCode, tolanguageCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_trans);

        fromSpinner = findViewById(R.id.fromspinner);
        toSpinner = findViewById(R.id.tospiner);
        sourceEdit = findViewById(R.id.editsourcetext);

        translateBtn = findViewById(R.id.btntranslate1);
        translatedTV = findViewById(R.id.translatedtext1);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromlanguages[position]);
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

        ArrayAdapter toAdapter = new ArrayAdapter(this, R.layout.spinneritem, tolanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        toSpinner.setAdapter(toAdapter);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if (sourceEdit.getText().toString().isEmpty()) {
                    Toast.makeText(LanguageTrans.this, "Please Enter your translate", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    Toast.makeText(LanguageTrans.this, "Please Select source language", Toast.LENGTH_SHORT).show();
                } else if (tolanguageCode == 0) {
                    Toast.makeText(LanguageTrans.this, "Please select the language to translate", Toast.LENGTH_SHORT).show();
                } else {
                    translateText(fromLanguageCode, tolanguageCode, sourceEdit.getText().toString());

                }
            }
        });

    }

    private void translateText(int fromLanguageCode, int tolanguageCode, String source) {
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
                        Toast.makeText(LanguageTrans.this, "Fail to translate : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LanguageTrans.this, "Fial to download Model " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language) {
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
                languageCode = 0;


        }
        return languageCode;
    }
}
