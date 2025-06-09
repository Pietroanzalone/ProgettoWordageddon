package com.example.progettowordageddon.model;

import java.time.LocalDateTime;

public class Record implements Comparable<Record> {
    private String username;
    private int punteggio;
    private Lingua lingua;
    private Difficolta difficolta;
    private LocalDateTime timestamp;

    public Record(String username, int punteggio, Lingua lingua, Difficolta difficolta, LocalDateTime timestamp) {
        this.username = username;
        this.punteggio = punteggio;
        this.lingua = lingua;
        this.difficolta = difficolta;
        this.timestamp = timestamp;
    }

    public Record(String username, Quiz quiz){
        this.username = username;
        this.punteggio = quiz.getPunteggio();
        this.lingua = quiz.getLingua();
        this.difficolta = quiz.getDifficolta();
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Record o) {
        int punt = punteggio - o.getPunteggio();
        int diff = difficolta.compareTo(o.difficolta);
        int data = timestamp.compareTo(o.getTimestamp());
        int user = username.compareTo(o.getUsername());

        if (punt != 0) return -punt;
        if (diff != 0) return -diff;
        if (data != 0) return -data;
        return user;
    }
}
