package com.example.calendar.Music;

import java.io.File;

public abstract class BaseResource {
    protected Music music;
    protected String url;
    protected Boolean loadState = false;
    protected File localResource;

    public BaseResource() {}

    public BaseResource(Music music, String url) {
        this.music = music;
        this.url = url;

        loadResource();
    }

    public void loadResource() {}

    private void setMusic(Music music) {
        this.music = music;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public static BaseResource getResourceFromUrl(Music music, String url, Class<? extends BaseResource> cls) throws InstantiationException, IllegalAccessException {
        BaseResource newResource = cls.newInstance();
        newResource.setMusic(music);
        newResource.setUrl(url);
        newResource.loadResource();
        return newResource;
    }
}
