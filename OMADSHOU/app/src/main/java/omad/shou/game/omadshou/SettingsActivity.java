package omad.shou.game.omadshou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer; // MediaPlayer obyekti
    private Switch switchMusic; // Music On/Off switch
    private Switch switchApplause; // Applause On/Off switch
    private LinearLayout llShare, llAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // XML fayl nomi

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sozlamalar");

        // Viewlarni topish
        switchMusic = findViewById(R.id.switchMusic);
        switchApplause = findViewById(R.id.switchApplause);
        llShare = findViewById(R.id.llshare);
        llAbout = findViewById(R.id.llAbout);

        // SharedPreferences dan holatlarni o‘qish
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isMusicOn = sharedPreferences.getBoolean("musicOn", false);
        boolean isApplauseOn = sharedPreferences.getBoolean("applauseOn", true);

        switchMusic.setChecked(isMusicOn);
        switchApplause.setChecked(isApplauseOn);

        // Agar musiqa yoqilgan bo‘lsa
        if (isMusicOn) {
            playMusic();
        }

        // Applause switch
        switchApplause.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("applauseOn", isChecked);
            editor.apply();
        });

        // Music switch
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("musicOn", isChecked);
            editor.apply();

            if (isChecked) {
                playMusic();
            } else {
                stopMusic();
            }
        });

        // About bosilganda
        llAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Dastur haqida")
                    .setMessage("Omad Shou o‘yini\n\n" +
                            "Bu o‘yinda siz 3 marta aylantirib sovg‘a olishingiz mumkin.\n" +
                            "Omad tilaymiz! 🎉")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Share bosilganda
        llShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            // Play Market link yoki APK link qo‘ying
            String appLink = "https://play.google.com/store/apps/details?id=omad.shou.game.omadshou";
            // Agar APK link bo‘lsa masalan:
            // String appLink = "https://drive.google.com/file/d/XXXXXX/view?usp=sharing";

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Omad Shou o‘yini");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Men 'Omad Shou' o‘yinini o‘ynayapman! Siz ham yuklab oling: \n\n" + appLink);

            startActivity(Intent.createChooser(shareIntent, "Ulashish"));
        });
    }

    private void playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.background);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
