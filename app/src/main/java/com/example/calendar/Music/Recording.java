package com.example.calendar.Music;

import java.io.File;

public class Recording extends BaseResource {

    @Override
    public void loadResource() {
        // leave this empty
    }

    public static Recording getRecordingFromUrl(Music music, String url) {
        try {
            return (Recording) BaseResource.getResourceFromUrl(music, url, Recording.class);
        } catch (Exception e) {
            return null;
        }
    }
}
