package net.yocxli.mediastorechecker.app;

import java.util.ArrayList;
import java.util.HashMap;

import net.yocxli.mediastorechecker.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class MediaStoreDetailFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>> {
    
    private static final String TAG = "MediaStoreDetailImageFragment";
    private static final boolean LOCAL_LOGV = true;
    
    public static final String TARGET_URI = "target.uri";
    
    private static class RecordLoader extends AbstractAsyncTaskLoader<ArrayList<HashMap<String, String>>> {
        private static final String TAG = "MediaStoreDetailImageFragment.RecordLoader";
        private static final boolean LOCAL_LOGV = true;
        
        final private Uri mUri;

        public RecordLoader(Context context, Uri uri) {
            super(context);
            mUri = uri;
        }

        @Override
        public ArrayList<HashMap<String, String>> loadInBackground() {
            if (LOCAL_LOGV) {
                Log.v(TAG, "loadInBackground: uri=" + mUri);
            }
            
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
            if (mUri != null && "content".equals(mUri.getScheme())) {
                ContentResolver cr = getContext().getContentResolver();
                Cursor c = cr.query(mUri, null, null, null, null);
                if (c != null) {
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        int num = c.getColumnCount();
                        for (int i = 0; i < num; i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("key", c.getColumnName(i));
                            map.put("value", c.getString(i));
                            list.add(map);
                        }
                    }
                    c.close();
                }
            }
            return list;
        }
        
    }
    
    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreate: savedInstanceState=" + savedInstanceState);
        }
        
        Bundle args = getArguments();
        mUri = (Uri) args.getParcelable(TARGET_URI);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (LOCAL_LOGV) {
            Log.v(TAG, "onActivityCreated: savedInstanceState=" + savedInstanceState);
        }
        
        setEmptyText(getActivity().getString(R.string.message_no_data));
        
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach: activity=" + activity);
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public Loader<ArrayList<HashMap<String, String>>> onCreateLoader(int id, Bundle args) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreateLoader");
        }
        return new RecordLoader(getActivity(), mUri);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<HashMap<String, String>>> loader, ArrayList<HashMap<String, String>> data) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onLoadFinished");
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, android.R.layout.simple_list_item_2,
                new String[] { "key", "value" }, new int[] { android.R.id.text1, android.R.id.text2});
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<HashMap<String, String>>> loader) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onLoaderReset");
        }
    }
    
}
