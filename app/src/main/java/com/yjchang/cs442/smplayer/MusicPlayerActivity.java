package com.yjchang.cs442.smplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class MusicPlayerActivity extends AppCompatActivity {

    @ViewById(R.id.music_title)
    TextView titleText;

    @ViewById(R.id.music_album)
    TextView albumText;

    @ViewById(R.id.music_artist)
    TextView artistText;

    @ViewById(R.id.music_cover)
    ImageView coverImage;

    private IBinder mBinder;
    private Uri curUri;
    private String curPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        curUri = getIntent().getData();
        curPath = getIntent().getStringExtra("path");
        getMusicMetadata();
    }

    @Background
    protected void getMusicMetadata() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(curPath);
        setMetadata(
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        );
        byte[] coverByteArr = mmr.getEmbeddedPicture();
        if (coverByteArr != null)
            setCoverImage(BitmapFactory.decodeByteArray(coverByteArr, 0, coverByteArr.length));
        else
            setCoverImage(R.drawable.ic_music_note_black_24dp);
    }

    @UiThread
    protected void setMetadata(String title, String album, String artist) {
        if (title != null)  titleText.setText(title);
        if (album != null)  albumText.setText(album);
        if (artist != null) artistText.setText(artist);
    }

    @UiThread
    protected void setCoverImage(Bitmap coverBitmap) {
        if (coverBitmap != null) coverImage.setImageBitmap(coverBitmap);
    }

    @UiThread
    protected void setCoverImage(int coverDrawableId) {
        Drawable coverDrawable =
                ContextCompat.getDrawable(getApplicationContext(), coverDrawableId);
        if(coverDrawable != null) coverImage.setImageDrawable(coverDrawable);
    }

}
