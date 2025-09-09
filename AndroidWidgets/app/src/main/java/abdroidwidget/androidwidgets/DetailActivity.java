package abdroidwidget.androidwidgets;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbarDetail);
        imageView = findViewById(R.id.widgetImage);
        textView = findViewById(R.id.widgetDescription);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("name");
        String desc = getIntent().getStringExtra("description");
        String imageName = getIntent().getStringExtra("image");

        getSupportActionBar().setTitle(name);

        textView.setText(desc);

        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            imageView.setImageResource(resId);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
