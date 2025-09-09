package com.example.russian_english_uzbek_dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);

        TextView tvAbout = findViewById(R.id.tv_about_text);

        if (langIndex == 0) { // English
            getSupportActionBar().setTitle("About Us");
            tvAbout.setText("This program will help you gain knowledge. The program contains 50 words.");
        } else if (langIndex == 1) { // Русский
            getSupportActionBar().setTitle("О приложении");
            tvAbout.setText("Эта программа поможет вам получить знания. Программа содержит 50 слов.");
        } else { // O'zbekcha
            getSupportActionBar().setTitle("Biz haqimizda");
            tvAbout.setText("Ushbu dastur sizga bilim olishga yordam beradi dasturda 50 ta so'z jamlangan.");
        }
    }
}
