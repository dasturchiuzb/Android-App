package com.example.russian_english_uzbek_dictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout llFontSize, llLanguage, llTheme;
    TextView tvFontSize, tvLanguage, tvTheme;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Toolbar sozlash
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Layoutlar
        llFontSize = findViewById(R.id.llFontSize);
        llLanguage = findViewById(R.id.llLanguage);
        llTheme = findViewById(R.id.llTheme);

        // TextViewlar
        tvFontSize = findViewById(R.id.tvFontSize);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvTheme = findViewById(R.id.tvTheme);

        applyLanguage(); // 📌 Toolbar va textlarni tilga moslash

        // Click listenerlar
        llFontSize.setOnClickListener(v -> showFontSizeDialog());
        llLanguage.setOnClickListener(v -> showLanguageDialog());
        llTheme.setOnClickListener(v -> showThemeDialog());
    }

    private void applyLanguage() {
        int langIndex = prefs.getInt("lang_index", 0);

        if (langIndex == 0) { // English
            getSupportActionBar().setTitle("Settings");
            tvFontSize.setText("Select Font Size");
            tvLanguage.setText("Select Language");
            tvTheme.setText("Select Theme");
        } else if (langIndex == 1) { // Русский
            getSupportActionBar().setTitle("Настройки");
            tvFontSize.setText("Выбрать размер шрифта");
            tvLanguage.setText("Выбрать язык");
            tvTheme.setText("Выбрать тему");
        } else { // O'zbekcha
            getSupportActionBar().setTitle("Sozlamalar");
            tvFontSize.setText("Font hajmini tanlash");
            tvLanguage.setText("Til tanlash");
            tvTheme.setText("Mavzu tanlash");
        }
    }

    private void showFontSizeDialog() {
        int langIndex = prefs.getInt("lang_index", 0);
        String[] sizes;
        String title, ok, cancel;

        if (langIndex == 0) {
            sizes = new String[]{"Small", "Medium", "Large"};
            title = "Select Font Size";
            ok = "OK";
            cancel = "Cancel";
        } else if (langIndex == 1) {
            sizes = new String[]{"Маленький", "Средний", "Большой"};
            title = "Выберите размер шрифта";
            ok = "ОК";
            cancel = "Отмена";
        } else {
            sizes = new String[]{"Kichik", "O‘rta", "Katta"};
            title = "Font hajmini tanlang";
            ok = "OK";
            cancel = "Bekor qilish";
        }

        int checked = prefs.getInt("font_size_index", 1);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(sizes, checked, (dialog, which) -> {
                    prefs.edit()
                            .putInt("font_size_index", which)
                            .putFloat("font_size",
                                    which == 0 ? 14f : which == 1 ? 18f : 22f)
                            .apply();
                })
                .setPositiveButton(ok, (d, w) -> goBackToMain())
                .setNegativeButton(cancel, null)
                .show();
    }

    private void showLanguageDialog() {
        int langIndex = prefs.getInt("lang_index", 0);
        String title, ok, cancel;
        String[] langs = {"English", "Русский", "O‘zbekcha"};

        if (langIndex == 0) {
            title = "Select Language";
            ok = "OK";
            cancel = "Cancel";
        } else if (langIndex == 1) {
            title = "Выберите язык";
            ok = "ОК";
            cancel = "Отмена";
        } else {
            title = "Til tanlang";
            ok = "OK";
            cancel = "Bekor qilish";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(langs, langIndex, (dialog, which) -> {
                    prefs.edit()
                            .putInt("lang_index", which)
                            .apply();
                })
                .setPositiveButton(ok, (d, w) -> goBackToMain())
                .setNegativeButton(cancel, null)
                .show();
    }

    private void showThemeDialog() {
        int langIndex = prefs.getInt("lang_index", 0);
        String[] themes;
        String title, ok, cancel;

        if (langIndex == 0) {
            themes = new String[]{"Light", "Dark"};
            title = "Select Theme";
            ok = "OK";
            cancel = "Cancel";
        } else if (langIndex == 1) {
            themes = new String[]{"Светлая", "Темная"};
            title = "Выберите тему";
            ok = "ОК";
            cancel = "Отмена";
        } else {
            themes = new String[]{"Yorug‘", "Tungi"};
            title = "Mavzuni tanlang";
            ok = "OK";
            cancel = "Bekor qilish";
        }

        int checked = prefs.getInt("theme_index", 0);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setSingleChoiceItems(themes, checked, (dialog, which) -> {
                    prefs.edit()
                            .putInt("theme_index", which)
                            .apply();
                })
                .setPositiveButton(ok, (d, w) -> goBackToMain())
                .setNegativeButton(cancel, null)
                .show();
    }

    private void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
