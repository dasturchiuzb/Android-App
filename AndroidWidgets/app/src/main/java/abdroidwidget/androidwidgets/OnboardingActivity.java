package abdroidwidget.androidwidgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    OnboardingAdapter adapter;
    LinearLayout llBack,llNext;
    TextView tvBack, tvNext;
    Button btnStart;

    SharedPreferences prefs;
    int currentLangIndex;

    // Tugmalar matni (3 tilda)
    private String[][] nextTexts = {
            { "Next", "Next", "Let's continue." },       // English
            { "Далее", "Далее", "Давайте продолжим." },   // Russian
            { "Keyingi", "Keyingi", "Davom etamiz" } // Uzbek
    };
    private String[][] backTexts = {
            { "Back", "Back", "Back" },   // English
            { "Назад", "Назад", "Назад" }, // Russian
            { "Orqaga", "Orqaga", "Orqaga" }  // Uzbek
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        currentLangIndex = prefs.getInt("lang_index", 2); // default Uzbek

        llBack = findViewById(R.id.llBack);
        llNext = findViewById(R.id.llNext);
        viewPager = findViewById(R.id.viewPager);
        tvBack = findViewById(R.id.tvBack);
        tvNext = findViewById(R.id.tvNext);
        btnStart = findViewById(R.id.btnStart);

        adapter = new OnboardingAdapter(this, currentLangIndex);
        viewPager.setAdapter(adapter);

        updateButtons(0);

        llNext.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            if (pos < adapter.getCount() - 1) {
                viewPager.setCurrentItem(pos + 1);
            } else {
                // Oxirgi sahifada → SplashActivity ga
                startActivity(new Intent(OnboardingActivity.this, SplashActivity.class));
                finish();
            }
        });

        llBack.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            if (pos > 0) {
                viewPager.setCurrentItem(pos - 1);
            }
        });

        btnStart.setOnClickListener(v -> {
            startActivity(new Intent(OnboardingActivity.this, SplashActivity.class));
            finish();
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float offset, int offsetPixels) {}
            @Override public void onPageSelected(int position) { updateButtons(position); }
            @Override public void onPageScrollStateChanged(int state) {}
        });
    }

    private void updateButtons(int position) {
        int lastIndex = adapter.getCount() - 1;
        // Next tugma matni
        tvNext.setText(nextTexts[currentLangIndex][position == lastIndex ? 2 : 0]);
        tvNext.setTextColor(getResources().getColor(R.color.next_text_color));

        // Back tugma matni
        tvBack.setText(backTexts[currentLangIndex][0]);
        tvBack.setTextColor(getResources().getColor(R.color.back_text_color));
        if (position == 0) {
            llBack.setVisibility(LinearLayout.INVISIBLE); // yoki GONE qilishingiz ham mumkin
        } else {
            llBack.setVisibility(LinearLayout.VISIBLE);
        }
        // Oxirgi sahifada Start ko‘rinadi
        if (position == lastIndex) {
            btnStart.setVisibility(Button.VISIBLE);
            tvNext.setVisibility(TextView.GONE);
            btnStart.setText(nextTexts[currentLangIndex][2]);
            btnStart.setBackgroundColor(getResources().getColor(R.color.start_button_color));
        } else {
            btnStart.setVisibility(Button.GONE);
            tvNext.setVisibility(TextView.VISIBLE);
        }
    }
}