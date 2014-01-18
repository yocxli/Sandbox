package net.yocxli.mediastorechecker.app;

import net.yocxli.mediastorechecker.R;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class MediaStoreFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnScrollListener {
    
    private static final String TAG = "MediaStoreFragment";
    private static final boolean LOCAL_LOGV = true;
    
    public static final String FRAGMENT_ARGUMENT_TARGET_URI = "target.uri";
    
    public static final String LOADER_ARGUMENT_PAGE = "page";
    
    public static final int AUTOLOAD_THRESHHOLD = 5;
    public static final int PAGE_SIZE           = 100;
    
    private SimpleCursorAdapter mAdapter;
    private Uri mUri;
    
    private boolean mIsLoading;
    private boolean mIsLoadedAll = false;
    private int mPage = 0;
    private int mLastItemCount;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        Bundle args = getArguments();
        mUri = (Uri) args.getParcelable(FRAGMENT_ARGUMENT_TARGET_URI);
        
        setEmptyText(getResources().getString(R.string.message_no_data));
        
        setListShown(false);
        
        mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2,
                null, getProjection(mUri), new int[] {android.R.id.text1, android.R.id.text2}, 0);
        
        setListAdapter(mAdapter);
        getListView().setOnScrollListener(this);
        
        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(LOADER_ARGUMENT_PAGE, mPage);
        getLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView text = (TextView) v.findViewById(android.R.id.text1);
        String mediaUri = mUri.toString();
        String mediaId = text.getText().toString();
        Intent intent = new Intent(getActivity(), MediaStoreDetailActivity.class);
        intent.setData(Uri.parse(mediaUri + "/" + mediaId));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreateLoader: id=" + id);
        }
        mIsLoading = true;
        
        int page = args.getInt(LOADER_ARGUMENT_PAGE, 0);
        String limit = String.valueOf(((page * PAGE_SIZE) + PAGE_SIZE));
        
        Uri.Builder builder = mUri.buildUpon();
        builder.appendQueryParameter("limit", limit);
        return new CursorLoader(getActivity(),
                builder.build(),
                getProjection(mUri),
                null, null, MediaStore.MediaColumns._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onLoadFinished: data=" + data);
        }
        mIsLoading = false;
        mAdapter.swapCursor(data);
        final int count = mAdapter.getCount();
        if (mLastItemCount == count) {
            // 前回読み込み時とアイテム数が一緒なら、全データ読み込み済みのフラグを立てる。
            mIsLoadedAll = true;
        }
        mLastItemCount = count;
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

    private static String[] getProjection(Uri uri) {
        String[] projection = new String[2];
        projection[0] = MediaStore.MediaColumns._ID;
        if (MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.equals(uri) || MediaStore.Audio.Albums.INTERNAL_CONTENT_URI.equals(uri)) {
            projection[1] = MediaStore.Audio.Albums.ALBUM;
        } else if (MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.equals(uri) || MediaStore.Audio.Artists.INTERNAL_CONTENT_URI.equals(uri)) {
            projection[1] = MediaStore.Audio.Artists.ARTIST;
        } else if (MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI.equals(uri) || MediaStore.Audio.Genres.INTERNAL_CONTENT_URI.equals(uri)) {
            projection[1] = MediaStore.Audio.Genres.NAME;
        } else {
            projection[1] = MediaStore.MediaColumns.DATA;
        }
        return projection;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0) {
            return;
        }
        if (!mIsLoadedAll && !mIsLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + AUTOLOAD_THRESHHOLD)) {
            mIsLoading = true;
            Bundle loaderArgs = new Bundle();
            loaderArgs.putInt(LOADER_ARGUMENT_PAGE, ++mPage);
            getLoaderManager().restartLoader(0, loaderArgs, this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
