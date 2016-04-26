package com.yjchang.cs442.smplayer.model;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjchang on 4/23/16.
 */
public class MediaFinder {

    public static File getBaseDir() {
        return new File("/storage/emulated/0");
    }

    public static MediaFileType findType(MediaFileEntry mfe) {
        if (mfe.getPath().isDirectory())
            return MediaFileType.DIRECTORY;

        Uri uri = Uri.fromFile(mfe.getPath());
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

        if (type == null) {
            Log.e("MediaFinder", "Unable to retrieve file type");
            return MediaFileType.OTHERS;
        }
        else if (type.toLowerCase().startsWith("audio/"))
            return MediaFileType.MUSIC;
        else if (type.toLowerCase().startsWith("video/"))
            return MediaFileType.VIDEO;
        return MediaFileType.OTHERS;
    }

    public static List<MediaFileEntry> findChildren(MediaFileEntry mfe) {
        if (mfe.getType() != MediaFileType.DIRECTORY)
            return null;

        File currentDir = mfe.getPath();
        List<MediaFileEntry> children = new ArrayList<MediaFileEntry>();

        if(currentDir.listFiles() == null) return children;

        for (File f : currentDir.listFiles()) {
            // prune folder with .nomedia
            if(".nomedia".equals(f.getName())) {
                mfe.setHasMedia(false);
                children.clear();
                break;
            }

            // jump unreadable, hidden files
            if (!f.exists() || !f.canRead() || f.isHidden())
                continue;

            MediaFileEntry child = new MediaFileEntry(f, mfe);
            if (child.getType() == MediaFileType.DIRECTORY && child.hasMedia())
                children.add(child);
            else if (child.getType().isMedia())
                children.add(child);
        }

        // TODO sorting

        return children;
    }
}
