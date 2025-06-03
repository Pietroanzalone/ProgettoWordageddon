package com.example.progettowordageddon.model;

public class SessioneQuiz {

    private static String difficolta;

    public static void setDifficolta(String diff) {
        SessioneQuiz.difficolta = diff;
    }

    public static String getDifficolta() {
        return difficolta;
    }
}
