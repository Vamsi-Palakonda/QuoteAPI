package com.example.quoteapi.service;

import com.example.quoteapi.model.Quote;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class QuoteService {
    private final List<Quote> quotes = Arrays.asList(
        new Quote("The only way to do great work is to love what you do. - Steve Jobs"),
new Quote("Believe you can and you're halfway there. - Theodore Roosevelt"),
new Quote("Success is not final, failure is not fatal: It is the courage to continue that counts. - Winston Churchill"),
new Quote("Don’t watch the clock; do what it does. Keep going. - Sam Levenson"),
new Quote("The harder I work, the luckier I get. - Gary Player"),
new Quote("Your time is limited, so don’t waste it living someone else’s life. - Steve Jobs"),
new Quote("Dream big and dare to fail. - Norman Vaughan"),
new Quote("Act as if what you do makes a difference. It does. - William James"),
new Quote("Hustle in silence and let your success make the noise. - Unknown"),
new Quote("What you get by achieving your goals is not as important as what you become by achieving your goals. - Zig Ziglar"),
new Quote("Don’t wait. The time will never be just right. - Napoleon Hill"),
new Quote("Do what you can, with what you have, where you are. - Theodore Roosevelt"),
new Quote("It always seems impossible until it’s done. - Nelson Mandela"),
new Quote("Everything you’ve ever wanted is on the other side of fear. - George Addair"),
new Quote("Start where you are. Use what you have. Do what you can. - Arthur Ashe"),
new Quote("The best way to predict the future is to create it. - Peter Drucker"),
new Quote("Success usually comes to those who are too busy to be looking for it. - Henry David Thoreau"),
new Quote("The secret of getting ahead is getting started. - Mark Twain"),
new Quote("Keep your face always toward the sunshine—and shadows will fall behind you. - Walt Whitman"),
new Quote("The difference between ordinary and extraordinary is that little extra. - Jimmy Johnson"),
new Quote("Don’t be pushed around by the fears in your mind. Be led by the dreams in your heart. - Roy T. Bennett"),
new Quote("Perseverance is failing 19 times and succeeding the 20th. - Julie Andrews"),
new Quote("Believe in yourself and all that you are. - Christian D. Larson"),
new Quote("Opportunities don’t happen, you create them. - Chris Grosser"),
new Quote("Don’t limit your challenges. Challenge your limits. - Unknown")

    );

    public Quote getRandomQuote() {
        return quotes.get(new Random().nextInt(quotes.size()));
    }
}