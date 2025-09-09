package com.example.russian_english_uzbek_dictionary.models;

public class Word {
    private String word;

    private String translation1;
    private String translation2;
    private String translation3;

    public Word(String word,String translation1, String translation2, String translation3) {
        this.word = word;
        this.translation1 = translation1;
        this.translation2 = translation2;
        this.translation3 = translation3;
    }

    public String getWord() { return word; }

    public String getTranslation1() { return translation1; }
    public String getTranslation2() { return translation2; }
    public String getTranslation3() { return translation3; }
}
