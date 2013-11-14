package net.yocxli.mediastorechecker.app;

import java.io.File;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class FileInfoFragment extends ListFragment {
    
    private static final String TAG = "FileInfoFragment";
    private static final boolean LOCAL_LOGV = true;
    
    public static final String DATA       = "data";
    
    private Uri mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onCreate: savedInstanceState=" + savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        mData = (Uri) args.getParcelable(DATA);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onActivityCreated: savedInstanceState=" + savedInstanceState);
        }
        super.onActivityCreated(savedInstanceState);
        
        update();
        
        setEmptyText(getActivity().getString(R.string.message_no_data));
    }
    
    private HashMap<String, String> createMap(String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        map.put("value", value);
        return map;
    }
    
    public void update() {
        final Activity activity = getActivity();
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        if (mData != null) {
            String path = mData.getPath();
            if ("contents".equals(mData.getScheme())) {
                ContentResolver cr = getActivity().getContentResolver();
                path = MediaStoreHelper.getFilePath(cr, mData);
            }
            File file = new File(path);
            list.add(createMap("path", file.getAbsolutePath()));
            list.add(createMap("exists", String.valueOf(file.exists())));
            list.add(createMap("is_directory", String.valueOf(file.isDirectory())));
            list.add(createMap("last modified", String.valueOf(file.lastModified())));
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
