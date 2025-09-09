package abdroidwidget.androidwidgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OnboardingAdapter extends PagerAdapter {
    private Context context;
    private int langIndex;

    // Rasmlar
    private int[] images = {
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo
    };

    // Title matnlari (3 tilda)
    private String[][] titles = {
            { "Welcome", "Learn Widgets", "Ready to Start?" }, // English
            { "Добро пожаловать", "Изучайте виджеты", "Готовы начать?" }, // Russian
            { "Xush kelibsiz", "Widgetlarni o‘rganing", "Boshlashga tayyormisiz?" } // Uzbek
    };

    // Description matnlari (3 tilda)
    private String[][] descriptions = {
            {
                    "This app will help you learn Android widgets.",
                    "Find detailed information about each widget.",
                    "Now press the button to get started!"
            },
            {
                    "Это приложение поможет вам изучить виджеты Android.",
                    "Вы найдете подробную информацию о каждом виджете.",
                    "Теперь нажмите кнопку, чтобы начать!"
            },
            {
                    "Bu dastur sizga Android widgetlarini o‘rganishga yordam beradi.",
                    "Har bir widget haqida batafsil ma’lumot topasiz.",
                    "Endi boshlash uchun tugmani bosing!"
            }
    };

    public OnboardingAdapter(Context context, int langIndex) {
        this.context = context;
        this.langIndex = langIndex;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_onboarding, container, false);

        ImageView imageView = view.findViewById(R.id.imgLogo);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDesc = view.findViewById(R.id.tvDesc);

        imageView.setImageResource(images[position]);
        tvTitle.setText(titles[langIndex][position]);
        tvDesc.setText(descriptions[langIndex][position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
