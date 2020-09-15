package com.aptitude.learning.e2buddy.ObjectClass;

public class WordPuzzleData {

    int id, wordid, wordSearchPuzzleId, status;
    String letter, wordSearchLetterImage, word;

    public WordPuzzleData(int id, int wordSearchPuzzleId, String letter, String wordSearchLetterImage) {
        this.id = id;
        this.wordSearchPuzzleId = wordSearchPuzzleId;
        this.letter = letter;
        this.wordSearchLetterImage = wordSearchLetterImage;
    }

    public WordPuzzleData(int wordid, int wordSearchPuzzleId, String word, int status) {
        this.wordid = wordid;
        this.wordSearchPuzzleId = wordSearchPuzzleId;
        this.word = word;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getWordSearchPuzzleId() {
        return wordSearchPuzzleId;
    }

    public String getLetter() {
        return letter;
    }

    public String getWordSearchLetterImage() {
        return wordSearchLetterImage;
    }

    public int getWordid() {
        return wordid;
    }

    public String getWord() {
        return word;
    }

    public int getStatus() {
        return status;
    }
}
