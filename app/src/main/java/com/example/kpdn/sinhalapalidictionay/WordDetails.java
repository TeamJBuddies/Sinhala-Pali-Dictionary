package com.example.kpdn.sinhalapalidictionay;

public class WordDetails {

private String meaning;
private String englishWord;
private String tens;
private int id;

    public WordDetails(String meaning, String englishWord, String tens, int id) {
        this.meaning = meaning;
        this.englishWord = englishWord;
        this.tens = tens;
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getTens() {
        return tens;
    }

    public int getId() {
        return id;
    }
}
