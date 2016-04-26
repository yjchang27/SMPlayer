package com.yjchang.cs442.smplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yjchang.cs442.smplayer.model.MediaFileEntry;
import com.yjchang.cs442.smplayer.model.MediaFileType;
import com.yjchang.cs442.smplayer.model.MediaFinder;
import com.yjchang.cs442.smplayer.service.MusicPlayService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MediaFileEntry mCurrentDir;

    @ViewById(R.id.textView)
    public TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Codes for Recycler View */
        mRecyclerView = (RecyclerView) findViewById(R.id.file_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Temporary Plug-in data
        List<MediaFileEntry> mMediaFileListing = new ArrayList<>();

        // specify an adapter (see also next example)
        mAdapter = new FileRecyclerAdapter(mMediaFileListing,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = mRecyclerView.getChildAdapterPosition(v);
                        MediaFileEntry mfe =
                                ((FileRecyclerAdapter)mAdapter).getMediaFileListing().get(pos);
                        if (mfe.getType() == MediaFileType.DIRECTORY)
                            changeMediaFileEntry(mfe);
                        else if (mfe.getType() == MediaFileType.MUSIC) {
                            // TODO start music player
                            Snackbar.make(mRecyclerView, "Playing MUSIC", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                            Uri uri = Uri.parse(mfe.getPath().toURI().toString());

                            // Start MusicPlayService
                            Intent intent =
                                    new Intent(MusicPlayService.ACTION_PLAY);
                            intent.setClass(getApplicationContext(), MusicPlayService.class);
                            intent.setData(uri);
                            startService(intent);

                            // Start MusicPlayerActivity
                            Intent intent2 =
                                    new Intent(getApplicationContext(), MusicPlayerActivity_.class);
                            intent2.setData(uri);
                            intent2.putExtra("path", mfe.getPath().getAbsolutePath());
                            startActivity(intent2);
                        }
                        else if (mfe.getType() == MediaFileType.VIDEO) {
                            // TODO start video player
                            Snackbar.make(mRecyclerView, "Attempt to play VIDEO", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }
                });
        mRecyclerView.setAdapter(mAdapter);

        recursiveFileSearch();
    }

    @Background
    public void recursiveFileSearch() {
        /** Codes for File Listing */
        File homeDir = MediaFinder.getBaseDir();
        MediaFileEntry mfe = new MediaFileEntry(homeDir, null);
        changeMediaFileEntry(mfe);
    }

    @UiThread
    public void changeMediaFileEntry(MediaFileEntry mfe) {
        mCurrentDir = mfe;
        mTextView.setText(mfe.getPath().getPath());
        ((FileRecyclerAdapter) mAdapter).setMediaFileListing(mfe.getChildren());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentDir.getParent() != null)
            changeMediaFileEntry(mCurrentDir.getParent());
        else
            super.onBackPressed();
    }

}
