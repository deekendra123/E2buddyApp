package com.aptitude.learning.e2buddy.ObjectClass;

public class WordData {
    int id;
    String date, word, meaning, usage, imagUrl;

    public WordData(int id, String date, String word, String meaning, String usage, String imagUrl) {
        this.id = id;
        this.date = date;
        this.word = word;
        this.meaning = meaning;
        this.usage = usage;
        this.imagUrl = imagUrl;
    }




    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagUrl() {
        return imagUrl;
    }

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
    }
}
