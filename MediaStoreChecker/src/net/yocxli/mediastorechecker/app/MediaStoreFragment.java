package net.yocxli.mediastorechecker.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MediaStoreFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final String TAG = "MediaStoreFragment";
    private static final boolean LOCAL_LOGV = false;
    
    public static final String TARGET_URI = "target.uri";
    
    private SimpleCursorAdapter mAdapter;
    private Uri mUri;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        Bundle args = getArguments();
        mUri = (Uri) args.getParcelable(TARGET_URI);
        
        setEmptyText("no data");
        
        setListShown(false);
        
        mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2,
                null, new String[] { MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA },
                new int[] {android.R.id.text1, android.R.id.text2}, 0);
        
        setListAdapter(mAdapter);
        
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView text = (TextView) v.findViewById(android.R.id.text2);
        Uri data = Uri.parse(String.valueOf(text.getText()));
        Intent intent = new Intent(getActivity(), MediaStoreDetailActivity.class);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreateLoader: id=" + id);
        }
        
        return new CursorLoader(getActivity(),
                mUri,
                new String[] { MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA },
                null, null, MediaStore.MediaColumns._ID + " DESC LIMIT 100");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onLoadFinished: data=" + data);
        }
        mAdapter.swapCursor(data);
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onLoaderReset: loader=" + loader);
        }
        mAdapter.swapCursor(null);
    }

    
}
