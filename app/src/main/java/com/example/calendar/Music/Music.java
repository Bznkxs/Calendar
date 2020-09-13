package com.example.calendar.Music;

public class Music {
    private final String title;
    private final Recording recording;
    private final Score score;

    public Music(String title, Recording recording, Score score) {
        this.title = title;
        this.recording = recording;
        this.score = score;
    }

    public Music(String title) {
        this.title = title;
        this.recording = null;
        this.score = null;
    }

    public Music(String title, String recording, String score) {
        this.title = title;
        this.recording = Recording.getRecordingFromUrl(this, recording);
        this.score = Score.getScoreFromUrl(this, score);
    }

    public String getTitle() {
        return title;
    }

    public Recording getRecording() {
        return recording;
    }

    public Score getScore() {
        return score;
    }
}
