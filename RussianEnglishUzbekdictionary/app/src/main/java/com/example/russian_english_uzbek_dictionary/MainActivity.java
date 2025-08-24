package com.example.russian_english_uzbek_dictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.russian_english_uzbek_dictionary.models.Word;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView;
    ActionBarDrawerToggle toggle;

    List<Word> wordList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.toolbar_title));
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        listView = findViewById(R.id.listView);
        TextView tvEmpty = findViewById(R.id.tvEmpty);
        listView.setEmptyView(tvEmpty);

        // ✅ Fast scroll qo'shish
        listView.setFastScrollEnabled(true);
        listView.setFastScrollAlwaysVisible(true);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        loadWordsFromJSON();
        applySettings();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = adapter.getItem(position);
            for (Word w : wordList) {
                if (w.getTranslation1().equals(selected) ||
                        w.getTranslation2().equals(selected) ||
                        w.getTranslation3().equals(selected)) {
                    showBottomSheet(w);
                    break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySettings();
    }

    private void loadWordsFromJSON() {
        try {
            InputStream is = getAssets().open("words.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Word word = new Word(
                        obj.getString("word"),
                        obj.getString("translation1"),
                        obj.getString("translation2"),
                        obj.getString("translation3")
                );
                wordList.add(word);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBottomSheet(Word word) {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_translation, null);
        TextView tv1 = view.findViewById(R.id.tvTranslation1);
        TextView tv2 = view.findViewById(R.id.tvTranslation2);
        TextView tv3 = view.findViewById(R.id.tvTranslation3);
        TextView btnClose = view.findViewById(R.id.btnClose);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int fontIndex = prefs.getInt("font_size_index", 1);
        float fontSize = (fontIndex == 0 ? 14f : fontIndex == 1 ? 18f : 22f);

        tv1.setTextSize(fontSize);
        tv2.setTextSize(fontSize);
        tv3.setTextSize(fontSize);
        btnClose.setTextSize(fontSize);

        tv1.setText(word.getTranslation1());
        tv2.setText(word.getTranslation2());
        tv3.setText(word.getTranslation3());

        int langIndex = prefs.getInt("lang_index", 0);
        if (langIndex == 0) btnClose.setText("Close");
        else if (langIndex == 1) btnClose.setText("Закрыть");
        else btnClose.setText("Yopish");

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void applySettings() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);
        float fontSize = prefs.getFloat("font_size", 18f);

        Menu menu = navigationView.getMenu();
        if (langIndex == 0) {
            menu.findItem(R.id.nav_settings).setTitle("Settings");
            menu.findItem(R.id.nav_about).setTitle("About Us");
            menu.findItem(R.id.nav_share).setTitle("Share");
            menu.findItem(R.id.nav_exit).setTitle("Exit");
        } else if (langIndex == 1) {
            menu.findItem(R.id.nav_settings).setTitle("Настройки");
            menu.findItem(R.id.nav_about).setTitle("О приложении");
            menu.findItem(R.id.nav_share).setTitle("Поделиться");
            menu.findItem(R.id.nav_exit).setTitle("Выход");
        } else {
            menu.findItem(R.id.nav_settings).setTitle("Sozlamalar");
            menu.findItem(R.id.nav_about).setTitle("Ilova haqida");
            menu.findItem(R.id.nav_share).setTitle("Ulashish");
            menu.findItem(R.id.nav_exit).setTitle("Chiqish");
        }

        // 🔹 Bo'sh ro'yxat TextView tilini moslash
        TextView tvEmpty = findViewById(R.id.tvEmpty);
        if (langIndex == 0) tvEmpty.setText("No words found!");
        else if (langIndex == 1) tvEmpty.setText("Слова не найдены!");
        else tvEmpty.setText("So‘z topilmadi!");

        if (getSupportActionBar() != null) {
            if (langIndex == 0) getSupportActionBar().setTitle("Dictionary");
            else if (langIndex == 1) getSupportActionBar().setTitle("Словарь");
            else getSupportActionBar().setTitle("Lug‘at");
        }

        List<String> words = new ArrayList<>();
        for (Word w : wordList) {
            if (langIndex == 0) words.add(w.getTranslation1());
            else if (langIndex == 1) words.add(w.getTranslation2());
            else words.add(w.getTranslation3());
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, words) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);
                SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
                float fontSize = prefs.getFloat("font_size", 18f);
                tv.setTextSize(fontSize);
                return v;
            }
        };
        listView.setAdapter(adapter);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);

        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
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

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);

        /// 🔹 SearchView hintini 3 tilga moslash
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (langIndex == 0) searchView.setQueryHint("Search words...");
        else if (langIndex == 1) searchView.setQueryHint("Поиск слов...");
        else searchView.setQueryHint("So‘zlarni qidiring...");

        // 🔹 Toolbar menyu tilini moslash
        if (langIndex == 0) {
            menu.findItem(R.id.action_favorites).setTitle("Favorites");
            menu.findItem(R.id.action_share).setTitle("Share");
            menu.findItem(R.id.action_exit).setTitle("Exit");
        } else if (langIndex == 1) {
            menu.findItem(R.id.action_favorites).setTitle("Избранное");
            menu.findItem(R.id.action_share).setTitle("Поделиться");
            menu.findItem(R.id.action_exit).setTitle("Выход");
        } else {
            menu.findItem(R.id.action_favorites).setTitle("Sevimlilar");
            menu.findItem(R.id.action_share).setTitle("Ulashish");
            menu.findItem(R.id.action_exit).setTitle("Chiqish");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int langIndex = prefs.getInt("lang_index", 0);
                List<String> filteredList = new ArrayList<>();
                for (Word w : wordList) {
                    if (w.getWord().toLowerCase().contains(newText.toLowerCase()) ||
                            w.getTranslation1().toLowerCase().contains(newText.toLowerCase()) ||
                            w.getTranslation2().toLowerCase().contains(newText.toLowerCase()) ||
                            w.getTranslation3().toLowerCase().contains(newText.toLowerCase())) {

                        if (langIndex == 0) filteredList.add(w.getTranslation1());
                        else if (langIndex == 1) filteredList.add(w.getTranslation2());
                        else filteredList.add(w.getTranslation3());
                    }
                }
                LinearLayout emptyLayout = findViewById(R.id.emptyLayout);

                if (filteredList.isEmpty()) {
                    listView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    adapter.clear();
                    adapter.addAll(filteredList);
                    adapter.notifyDataSetChanged();
                }
                adapter.clear();
                adapter.addAll(filteredList);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int langIndex = prefs.getInt("lang_index", 0);
        int id = item.getItemId();
        if (id == R.id.action_favorites) {
            return true;
        } else if (id == R.id.action_exit) {
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

        } else if (id == R.id.action_share) {
            shareAppApk();
        }
        return super.onOptionsItemSelected(item);
    }
}