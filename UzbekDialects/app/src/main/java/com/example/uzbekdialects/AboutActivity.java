package com.example.uzbekdialects;

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
            tvAbout.setText("This program contains meanings of the Uzbek dialect.");
        } else if (langIndex == 1) { // Русский
            getSupportActionBar().setTitle("О приложении");
            tvAbout.setText("В этой программе содержатся значения узбекского диалекта.");
        } else { // O'zbekcha
            getSupportActionBar().setTitle("Biz haqimizda");
            tvAbout.setText("Ushbu dasturda o'zbek shevasining ma'nolari jamlangan.");
        }
    }
}
