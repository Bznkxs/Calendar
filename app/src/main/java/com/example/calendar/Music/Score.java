package com.example.calendar.Music;

import java.io.File;

public class Score extends BaseResource {

    @Override
    public void loadResource() {
        // leave this empty
    }

    public static Score getScoreFromUrl(Music music, String url) {
        try {
            return (Score)BaseResource.getResourceFromUrl(music, url, Score.class);
        } catch (Exception e) {
            return null;
        }
    }
}
