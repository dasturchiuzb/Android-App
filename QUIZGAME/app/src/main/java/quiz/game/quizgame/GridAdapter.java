package quiz.game.quizgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    private final String[] categoryNames;
    private final int[] categoryImages;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] categoryNames, int[] categoryImages) {
        this.context = context;
        this.categoryNames = categoryNames;
        this.categoryImages = categoryImages;
    }

    @Override
    public int getCount() {
        return categoryNames.length;
    }

    @Override
    public Object getItem(int position) {
        return categoryNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.itemImage);
        TextView textView = convertView.findViewById(R.id.itemText);

        imageView.setImageResource(categoryImages[position]);
        textView.setText(categoryNames[position]);

        return convertView;
    }
}
