package com.example.russian_english_uzbek_dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.history_toolbar);
        tvAbout = findViewById(R.id.tv_about_text);

        setSupportActionBar(toolbar);

        // Back tugmasini faollashtirish
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Tilga qarab Toolbar title va TextView matnini yangilash
        applySettings();
    }

    private void applySettings() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);

        if (getSupportActionBar() != null && tvAbout != null) {
            if (langIndex == 0) { // English
                getSupportActionBar().setTitle("History");
                tvAbout.setText("About us: This app allows you to translate words.");
            } else if (langIndex == 1) { // Russian
                getSupportActionBar().setTitle("История");
                tvAbout.setText("О нас: Это приложение позволяет переводить слова.");
            } else { // Uzbek
                getSupportActionBar().setTitle("Tarix");
                tvAbout.setText("Biz haqimizda: Bu ilova sizga so'zlarni tarjima qilish imkonini beradi.");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // back tugmasi bosilganda activity yopiladi
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
