package com.example.quoteapi.model;

public class Quote {
    private String text;

    public Quote(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}