package com.example.russian_english_uzbek_dictionary;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalHelper {
    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
