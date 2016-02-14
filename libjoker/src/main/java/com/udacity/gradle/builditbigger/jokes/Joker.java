package com.udacity.gradle.builditbigger.jokes;

import java.util.Random;

public class Joker {

    public static String[] JOKES = {
            "What do you call an Amish guy with his hand in a horse\'s mouth? A mechanic.",
            "What do you call it when Batman skips church? Christian Bale.",
            "What do you call a group of unorganized cats? A cat-astrophe.",
            "What disease do you get when you decorate for Christmas? Tinselitus.",
            "Why don't they play poker in the jungle? Too many cheetahs."
    };

    public String getJoke(){
        return JOKES[new Random().nextInt(JOKES.length)];
    }
}
