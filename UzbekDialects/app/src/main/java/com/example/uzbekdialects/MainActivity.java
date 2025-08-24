package com.example.uzbekdialects;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> filteredTitles = new ArrayList<>();
    ArrayAdapter<String> adapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView;
    ActionBarDrawerToggle toggle;

    SharedPreferences prefs;
    float fontSize; // font hajmi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.toolbar_title));
        }
        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        applySettings(); // Drawer menyu tillarini yangilash

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ListView
        listView = findViewById(R.id.listView);
        loadJson();
        filteredTitles.addAll(titles);

        // Font hajmini yuklash va adapterni o‘rnatish
        loadFontSize();
        setAdapter();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            int index = titles.indexOf(filteredTitles.get(i));
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("title", titles.get(index));
            intent.putExtra("description", descriptions.get(index));
            startActivity(intent);
        });

        // Drawer item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            int langIndex = prefs.getInt("lang_index", 0);
            if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            } else if (id == R.id.nav_share) {
                shareAppApk();
            } else if (id == R.id.nav_exit) {
                String title, message, yes, no;
                if (langIndex == 0) {
                    title = "Exit";
                    message = "Do you really want to exit the app?";
                    yes = "Yes";
                    no = "No";
                } else if (langIndex == 1) {
                    title = "Выход";
                    message = "Вы действительно хотите выйти из приложения?";
                    yes = "Да";
                    no = "Нет";
                } else {
                    title = "Chiqish";
                    message = "Ilovadan chiqishni istaysizmi?";
                    yes = "Ha";
                    no = "Yo‘q";
                }
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(yes, (dialog, which) -> finish())
                        .setNegativeButton(no, null)
                        .show();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }
    private void shareAppApk() {
        try {
            String appPath = getApplicationInfo().sourceDir;
            File file = new File(appPath);

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/vnd.android.package-archive");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "APK ulashish"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applySettings() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0); // 0-English, 1-Russian, 2-Uzbek

        Menu menu = navigationView.getMenu();

        switch (langIndex) {
            case 0: // English
                menu.findItem(R.id.nav_settings).setTitle("Settings");
                menu.findItem(R.id.nav_about).setTitle("About");
                menu.findItem(R.id.nav_share).setTitle("Share");
                menu.findItem(R.id.nav_exit).setTitle("Exit");
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Uzbek Dialects");
                break;
            case 1: // Russian
                menu.findItem(R.id.nav_settings).setTitle("Настройки");
                menu.findItem(R.id.nav_about).setTitle("О приложении");
                menu.findItem(R.id.nav_share).setTitle("Поделиться");
                menu.findItem(R.id.nav_exit).setTitle("Выход");
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Узбекские диалекты");
                break;
            case 2: // Uzbek
            default:
                menu.findItem(R.id.nav_settings).setTitle("Sozlamalar");
                menu.findItem(R.id.nav_about).setTitle("Ilova haqida");
                menu.findItem(R.id.nav_share).setTitle("Ulashish");
                menu.findItem(R.id.nav_exit).setTitle("Chiqish");
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("O‘zbek shevalari");

                break;
        }
    }

    // Fontni yuklash
    private void loadFontSize() {
        fontSize = prefs.getFloat("font_size", 18f);
    }

    // Adapterni yaratish
    private void setAdapter() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredTitles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(fontSize); // tanlangan fontni qo‘llash
                return textView;
            }
        };
        listView.setAdapter(adapter);
    }

    // JSON fayldan shevalarni yuklash
    private void loadJson() {
        try {
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            int langIndex = prefs.getInt("lang_index", 0); // 0-English, 1-Russian, 2-Uzbek

            String fileName;
            if (langIndex == 0) {
                fileName = "shevalar_en.json"; // Inglizcha
            } else if (langIndex == 1) {
                fileName = "shevalar_ru.json"; // Ruscha
            } else {
                fileName = "shevalar_uz.json"; // O‘zbekcha
            }

            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            titles.clear();
            descriptions.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                titles.add(obj.getString("title"));
                descriptions.add(obj.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Toolbar search qo‘shish
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Tilga mos qilib search hint berish
        int langIndex = prefs.getInt("lang_index", 0);
        if (langIndex == 0) {
            searchView.setQueryHint("Search");
        } else if (langIndex == 1) {
            searchView.setQueryHint("Поиск");
        } else {
            searchView.setQueryHint("Qidirish");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredTitles.clear();
                for (String title : titles) {
                    if (title.toLowerCase().contains(newText.toLowerCase())) {
                        filteredTitles.add(title);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Qaytganda font size yangilash
        loadFontSize();
        setAdapter();
    }
}
