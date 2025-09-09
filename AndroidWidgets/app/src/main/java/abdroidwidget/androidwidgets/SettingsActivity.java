package abdroidwidget.androidwidgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvLanguage, tvAbout, tvShare, tvExit, settingsTitle, settingsSubtitle;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private int currentLangIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar
        toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // TextViewlar
        tvLanguage = findViewById(R.id.tvLanguage);
        tvAbout = findViewById(R.id.tvAbout);
        tvShare = findViewById(R.id.tvShare);
        tvExit = findViewById(R.id.tvExit);
        settingsTitle = findViewById(R.id.settingsTitle);
        settingsSubtitle = findViewById(R.id.settingsSubtitle);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentLangIndex = prefs.getInt("lang_index", 2);

        updateTexts(currentLangIndex);

        // LinearLayout itemlar
        LinearLayout itemLanguage = findViewById(R.id.itemLanguage);
        LinearLayout itemAbout = findViewById(R.id.itemAbout);
        LinearLayout itemShare = findViewById(R.id.itemShare);
        LinearLayout itemExit = findViewById(R.id.itemExit);

        // SharedPreferences
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentLangIndex = prefs.getInt("lang_index", 2); // default = Uzbek

        // Dastlab matnlarni yangilash
        updateTexts(currentLangIndex);

        // Tilni tanlash
        itemLanguage.setOnClickListener(v -> {
            final int[] selectedIndex = {currentLangIndex};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this,
                    android.R.layout.select_dialog_singlechoice);
            adapter.addAll(getLanguageNames(currentLangIndex));

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle(getDialogTitle(currentLangIndex));

            builder.setSingleChoiceItems(adapter, selectedIndex[0], (dialogInterface, which) -> {
                selectedIndex[0] = which;

                adapter.clear();
                adapter.addAll(getLanguageNames(which));
                ((AlertDialog) dialogInterface).setTitle(getDialogTitle(which));

                // Toolbar va barcha TextViewlarni darhol yangilash
                updateTexts(which);
            });

            builder.setPositiveButton(getTextByLang("OK", currentLangIndex), (dialogInterface, which) -> {
                currentLangIndex = selectedIndex[0];
                prefs.edit().putInt("lang_index", currentLangIndex).apply();
                updateTexts(currentLangIndex);
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });

            builder.setNegativeButton(getTextByLang("Cancel", currentLangIndex), null);
            builder.show();
        });

        // Dastur haqida
        itemAbout.setOnClickListener(v -> showAboutDialog());

        // Ulashish
        itemShare.setOnClickListener(v -> shareApp());

        // Chiqish
        itemExit.setOnClickListener(v -> showExitDialog());
    }

    private void updateTexts(int langIndex) {
        currentLangIndex = langIndex;
        prefs.edit().putInt("lang_index", langIndex).apply();

        switch (langIndex) {
            case 0: // English
                getSupportActionBar().setTitle("Android Widgets Version 1.0");
                settingsTitle.setText("Android Widgets");
                settingsSubtitle.setText("Version 1.0");
                tvLanguage.setText("🌐 Languages");
                tvAbout.setText("ℹ️ About App");
                tvShare.setText("🔗 Share App");
                tvExit.setText("🚪 Exit");
                break;
            case 1: // Russian
                getSupportActionBar().setTitle("Андроид Виджеты Версия 1.0");
                settingsTitle.setText("Андроид Виджеты");
                settingsSubtitle.setText("Версия 1.0");
                tvLanguage.setText("🌐 Языки");
                tvAbout.setText("ℹ️ О приложении");
                tvShare.setText("🔗 Поделиться");
                tvExit.setText("🚪 Выход");
                break;
            default: // Uzbek
                getSupportActionBar().setTitle("Android Widgets Versiyasi 1.0");
                settingsTitle.setText("Android Widgets");
                settingsSubtitle.setText("Versiya 1.0");
                tvLanguage.setText("🌐 Tillar");
                tvAbout.setText("ℹ️ Dastur haqida");
                tvShare.setText("🔗 Dastur ulashish");
                tvExit.setText("🚪 Chiqish");
                break;
        }
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(getTextByLang("About App", currentLangIndex));
        builder.setMessage(getAboutText(currentLangIndex));
        builder.setPositiveButton(getTextByLang("OK", currentLangIndex), null);
        builder.setNegativeButton(getTextByLang("Cancel", currentLangIndex), null);
        builder.show();
    }

    private void shareApp() {
        try {
            String apkPath = getApplicationInfo().sourceDir;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/vnd.android.package-archive");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
            startActivity(Intent.createChooser(shareIntent, getTextByLang("Share App", currentLangIndex)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(getTextByLang("Exit", currentLangIndex));
        builder.setMessage(getTextByLang("Are you sure you want to exit?", currentLangIndex));
        builder.setPositiveButton(getTextByLang("OK", currentLangIndex), (dialog, which) -> finishAffinity());
        builder.setNegativeButton(getTextByLang("Cancel", currentLangIndex), null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        });
        dialog.show();
    }

    private String getDialogTitle(int langIndex) {
        switch (langIndex) {
            case 0: return "Select Language";
            case 1: return "Выберите язык";
            default: return "Tilni tanlang";
        }
    }

    private String[] getLanguageNames(int langIndex) {
        switch (langIndex) {
            case 0: return new String[]{"English", "Russian", "Uzbek"};
            case 1: return new String[]{"Инглиш", "Русский", "Узбекский"};
            default: return new String[]{"Inglizcha", "Ruscha", "O‘zbekcha"};
        }
    }

    private String getAboutText(int langIndex) {
        switch (langIndex) {
            case 0: return "Android Widgets\n\nThis app provides information about various Android widgets.";
            case 1: return "Андроид Виджеты\n\nЭто приложение предоставляет информацию о различных Android виджетах.";
            default: return "Android Widgets\n\nBu dastur turli Android vidjetlari haqida ma’lumot beradi.";
        }
    }

    private String getTextByLang(String text, int langIndex) {
        switch (text) {
            case "OK":
                switch (langIndex) { case 0: return "OK"; case 1: return "ОК"; default: return "OK"; }
            case "Cancel":
                switch (langIndex) { case 0: return "Cancel"; case 1: return "Отмена"; default: return "Bekor qilish"; }
            case "Share App":
                switch (langIndex) { case 0: return "Share App"; case 1: return "Поделиться"; default: return "Dastur ulashish"; }
            case "About App":
                switch (langIndex) { case 0: return "About App"; case 1: return "О приложении"; default: return "Dastur haqida"; }
            case "Exit":
                switch (langIndex) { case 0: return "Exit"; case 1: return "Выход"; default: return "Chiqish"; }
            case "Are you sure you want to exit?":
                switch (langIndex) { case 0: return "Are you sure you want to exit?"; case 1: return "Вы уверены, что хотите выйти?"; default: return "Dasturdan chiqmoqchimisiz?"; }
            default: return text;
        }
    }
}
