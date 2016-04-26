package com.yjchang.cs442.smplayer.model;

import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by yjchang on 4/23/16.
 */
public class MediaFileEntry {

    private File path;
    private String name;
    private boolean hasMedia = false;
    private MediaFileType type;
    private MediaFileEntry parent;
    private List<MediaFileEntry> children;

    public MediaFileEntry(File path, MediaFileEntry parent) {
        this.path = path;
        name = path.getName();
        this.parent = parent;
        setType(MediaFinder.findType(this));
        if (type == MediaFileType.DIRECTORY)
            setChildren(MediaFinder.findChildren(this));
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasMedia() {
        return hasMedia;
    }

    public void setHasMedia(boolean hasMedia) {
        if (type == MediaFileType.DIRECTORY) {
            this.hasMedia = hasMedia;
            if (parent != null && !parent.hasMedia())
                parent.setHasMedia(true);
        }
    }

    public MediaFileType getType() {
        return type;
    }

    public void setType(MediaFileType type) {
        this.type = type;
        if (type.isMedia() && parent != null)
            parent.setHasMedia(true);
    }

    public MediaFileEntry getParent() {
        return parent;
    }

    public void setParent(MediaFileEntry parent) {
        this.parent = parent;
    }

    public List<MediaFileEntry> getChildren() {
        return children;
    }

    public void setChildren(List<MediaFileEntry> children) {
        this.children = children;
    }
}
