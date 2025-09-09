package quiz.game.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GridView gridView;

    String[] categories = {
            "Matematika",
            "Tarix",
            "Sport",
            "Geografiya"
    };

    int[] images = {
            R.drawable.math,     // drawable ichiga math.png joyla
            R.drawable.history,  // history.png
            R.drawable.sport,    // sport.png
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        GridAdapter adapter = new GridAdapter(this, categories, images);
        gridView.setAdapter(adapter);

        // GridView item bosilganda QuizActivity ochiladi
        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("category", categories[position]); // kategoriya yuboriladi
            startActivity(intent);
        });
    }
}
