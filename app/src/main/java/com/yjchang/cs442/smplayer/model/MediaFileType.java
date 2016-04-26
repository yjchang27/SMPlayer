package com.yjchang.cs442.smplayer.model;

/**
 * Created by yjchang on 4/23/16.
 */
public enum MediaFileType {
    MUSIC(true),
    VIDEO(true),
    DIRECTORY(false),
    OTHERS(false);

    private final boolean isMedia;
    MediaFileType(final boolean isMedia) { this.isMedia = isMedia; }
    public boolean isMedia() { return isMedia;}
}
