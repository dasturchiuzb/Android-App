package abdroidwidget.androidwidgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    int progressStatus = 0;
    Handler handler = new Handler();
    TextView loadingText;
    int dotCount = 0;
    SharedPreferences prefs;
    int currentLangIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImage = findViewById(R.id.splashImage);
        TextView name = findViewById(R.id.name);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentLangIndex = prefs.getInt("lang_index", 2); // default = Uzbek
        name.setText(getAppName(currentLangIndex));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dotCount = (dotCount + 1) % 4; // 0 → 1 → 2 → 3 → qaytadi
                String dots = new String(new char[dotCount]).replace("\0", ".");
                loadingText.setText(getLoadingText(currentLangIndex) + dots);
                handler.postDelayed(this, 500); // yarim soniyada bitta o‘zgaradi
            }
        }, 500);

        // Progressni sekin-asta oshirib boramiz
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus ++;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(50); // har 0.1 soniyada oshadi
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Progress tugagach MainActivity ga o'tadi
            runOnUiThread(() -> {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            });
        }).start();
    }
    private String getLoadingText(int langIndex) {
        switch (langIndex) {
            case 0: return "Loading";      // English
            case 1: return "Загрузка";     // Russian
            default: return "Yuklanmoqda"; // Uzbek
        }
    }
    private String getAppName(int langIndex) {
        switch (langIndex) {
            case 0: return "ANDROID WIDGETS";        // English
            case 1: return "АНДРОИД ВИДЖЕТЫ";       // Russian
            default: return "ANDROID VIDJETLAR";    // Uzbek
        }
    }
}
