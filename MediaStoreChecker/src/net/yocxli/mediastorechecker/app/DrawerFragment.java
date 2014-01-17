package net.yocxli.mediastorechecker.app;

import net.yocxli.mediastorechecker.Const;
import net.yocxli.mediastorechecker.R;
import net.yocxli.mediastorechecker.util.MediaStoreHelper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerFragment extends ListFragment {
    private OnNavigationClickListener mListener;
    
    private static final int[] MENU = {
        R.string.label_image,
        R.string.label_image_thumbnail,
        R.string.label_audio,
        R.string.label_audio_album,
        R.string.label_audio_artist,
        R.string.label_audio_genre,
        R.string.label_audio_playlist,
        R.string.label_video,
        R.string.label_video_thumbnail,
        R.string.label_file,
    };
    
    public interface OnNavigationClickListener {
        public void onNavigationClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.navigation_drawer, null);
        
        final int count = MENU.length;
        final int size = MediaStoreHelper.hasFileTable() ? count : count - 1;
        String[] data = new String[size];
        for (int i = 0; i < count; i++) {
            if (MENU[i] == R.string.label_file && !MediaStoreHelper.hasFileTable()) {
                continue;
            }
            data[i] = getString(MENU[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, data);
        
        setListAdapter(adapter);
        
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        int positionRes = MENU[position];
        int type = -1;
        switch (positionRes) {
            case R.string.label_image:
                type = Const.MEDIA_TYPE_IMAGE;
                break;
            case R.string.label_image_thumbnail:
                type = Const.MEDIA_TYPE_IMAGE_THUMBNAIL;
                break;
            case R.string.label_audio:
                type = Const.MEDIA_TYPE_AUDIO;
                break;
            case R.string.label_audio_album:
                type = Const.MEDIA_TYPE_AUDIO_ALBUM;
                break;
            case R.string.label_audio_artist:
                type = Const.MEDIA_TYPE_AUDIO_ARTIST;
                break;
            case R.string.label_audio_genre:
                type = Const.MEDIA_TYPE_AUDIO_GENRE;
                break;
            case R.string.label_audio_playlist:
                type = Const.MEDIA_TYPE_AUDIO_PLAYLIST;
                break;
            case R.string.label_video:
                type = Const.MEDIA_TYPE_VIDEO;
                break;
            case R.string.label_video_thumbnail:
                type = Const.MEDIA_TYPE_VIDEO_THUMBNAIL;
                break;
            case R.string.label_file:
                type = Const.MEDIA_TYPE_FILE;
                break;
        }
        
        Intent intent = new Intent(getActivity(), MediaStoreTableActivity.class);
        intent.putExtra(Const.MEDIA_TYPE, type);
        startActivity(intent);
        
        if (mListener != null) {
            mListener.onNavigationClick();
        }
    }

    public void setOnNavigationClickListener(OnNavigationClickListener listener) {
        mListener = listener;
    }
}
