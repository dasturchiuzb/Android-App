package abdroidwidget.androidwidgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    List<Widget> widgetList = new ArrayList<>();
    List<String> widgetNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Android Widgets");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

// Title oq
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

// Back icon oq
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }

// Uch nuqta oq
        if (toolbar.getOverflowIcon() != null) {
            toolbar.getOverflowIcon().setTint(getResources().getColor(android.R.color.white));
        }


        // back tugmani bosganda chiqish
        toolbar.setNavigationOnClickListener(v -> finish());

        listView = findViewById(R.id.listViewWidgets);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Widget selectedWidget = widgetList.get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("name", selectedWidget.getName());
            intent.putExtra("description", selectedWidget.getDescription());
            intent.putExtra("image", selectedWidget.getImage());
            startActivity(intent);
        });
    }
    // 🔹 onResume() alohida bo‘lishi kerak
    @Override
    protected void onResume() {
        super.onResume();
        loadWidgetsFromJSON();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, widgetNames);
        listView.setAdapter(adapter);
    }
    private void loadWidgetsFromJSON() {
        try {
            widgetList.clear();
            widgetNames.clear();
            // Til sozlamasi SharedPreferences orqali olinadi
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            int langIndex = prefs.getInt("lang_index", 2); // 0-English, 1-Russian, 2-Uzbek

            String fileName;
            if (langIndex == 0) {
                fileName = "widgets_en.json"; // Inglizcha
            } else if (langIndex == 1) {
                fileName = "widgets_ru.json"; // Ruscha
            } else {
                fileName = "widgets_uz.json"; // O‘zbekcha
            }

            // JSON faylni assets papkadan o‘qish
            AssetManager manager = getAssets();
            InputStream is = manager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            // JSON arrayni o‘qib Listga qo‘shish
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Widget w = new Widget(
                        obj.getString("name"),
                        obj.getString("description"),
                        obj.getString("image")
                );
                widgetList.add(w);
                widgetNames.add(w.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // 🔹 Menu item bosilganda ishlaydi
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
