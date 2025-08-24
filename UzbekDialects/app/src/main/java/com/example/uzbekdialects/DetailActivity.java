package com.example.uzbekdialects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    SharedPreferences prefs;
    float fontSize; // font hajmi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        fontSize = prefs.getFloat("font_size", 18f);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        getSupportActionBar().setTitle(title);

        TextView textView = findViewById(R.id.textView);
        textView.setTextSize(fontSize); // tanlangan fontni qo‘llash

        try {
            // description JSON array ekanligini tekshiramiz
            JSONArray dialectsArray = new JSONArray(description);
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < dialectsArray.length(); i++) {
                JSONObject obj = dialectsArray.getJSONObject(i);
                builder.append("📍 ")
                        .append(obj.getString("region"))
                        .append(" → ")
                        .append(obj.getString("word"))
                        .append("\n\n");
            }

            textView.setText(builder.toString());

        } catch (Exception e) {
            // Agar oddiy matn bo‘lsa, shuni ko‘rsatadi
            textView.setText(description);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // qaytganda fontni yangilash
        fontSize = prefs.getFloat("font_size", 18f);
        TextView textView = findViewById(R.id.textView);
        textView.setTextSize(fontSize);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back tugmasi bosilganda MainActivity ga qaytish
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
