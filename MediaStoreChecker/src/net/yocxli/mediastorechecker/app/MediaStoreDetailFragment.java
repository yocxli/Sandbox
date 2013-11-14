package net.yocxli.mediastorechecker.app;

import java.util.ArrayList;
import java.util.HashMap;

import net.yocxli.mediastorechecker.R;
import net.yocxli.mediastorechecker.util.MediaStoreHelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class MediaStoreDetailFragment extends ListFragment {
    
    private static final String TAG = "MediaStoreDetailImageFragment";
    private static final boolean LOCAL_LOGV = true;
    
    public static final String TARGET_URI = "target.uri";
    public static final String DATA       = "data";
    
    private Uri mUri;
    private Uri mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreate: savedInstanceState=" + savedInstanceState);
        }
        
        Bundle args = getArguments();
        mUri  = (Uri) args.getParcelable(TARGET_URI);
        mData = (Uri) args.getParcelable(DATA);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (LOCAL_LOGV) {
            Log.v(TAG, "onActivityCreated: savedInstanceState=" + savedInstanceState);
        }
        
        update();
        
        setEmptyText(getActivity().getString(R.string.message_no_data));
    }
    
    public void update() {
        final Activity activity = getActivity();
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        if (mUri != null && mData != null) {
            ContentResolver cr = getActivity().getContentResolver();
            String path = mData.getPath();
            if ("content".equals(mData.getScheme())) {
                path = MediaStoreHelper.getFilePath(cr, mData);
            }
            Cursor c = cr.query(mUri, null, MediaStore.MediaColumns.DATA + " = ?",
                    new String[] { path }, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int num = c.getColumnCount();
                boolean first = true;
                do {
                    StringBuffer buf = new StringBuffer();
                for (int i = 0; i < num; i++) {
                    if (!first) {
                        String v = c.getString(i);
                        if (!TextUtils.isEmpty(v)) {
                            buf.append(", " + c.getColumnName(i) + "=" + v);
                        }
                        continue;
                    }
                    String v = c.getString(i);
                    if (!TextUtils.isEmpty(v)) {
                        buf.append(", " + c.getColumnName(i) + "=" + v);
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("key", c.getColumnName(i));
                    map.put("value", c.getString(i));
                    list.add(map);
                }
                first = false;
                Log.d(TAG, buf.toString());
                } while (c.moveToNext());
                c.close();
            }
        }
        
        SimpleAdapter adapter = new SimpleAdapter(activity, list, android.R.layout.simple_list_item_2,
                new String[] { "key", "value" }, new int[] { android.R.id.text1, android.R.id.text2});
        setListAdapter(adapter);
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
    
    
}
