package com.yjchang.cs442.smplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjchang.cs442.smplayer.model.MediaFileEntry;

import java.util.List;

/**
 * Created by yjchang on 4/23/16.
 */
public class FileRecyclerAdapter extends RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder> {
    private List<MediaFileEntry> mFileListing;
    private View.OnClickListener mOnClickListner;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FileRecyclerAdapter(List<MediaFileEntry> fileListing,
                               View.OnClickListener onClickListener) {
        mFileListing = fileListing;
        mOnClickListner = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FileRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file_recycler, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // --> Done in layout
        v.setOnClickListener(mOnClickListner);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mFileListing.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFileListing.size();
    }

    public List<MediaFileEntry> getMediaFileListing() {
        return mFileListing;
    }

    public void setMediaFileListing(List<MediaFileEntry> mFileListing) {
        this.mFileListing = mFileListing;
    }
}
