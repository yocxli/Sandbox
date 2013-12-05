package net.yocxli.mediastorechecker.app;

import net.yocxli.mediastorechecker.Const;
import net.yocxli.mediastorechecker.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class DrawerFragment extends Fragment implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.navigation_drawer, null);
        view.findViewById(R.id.nav_button_image).setOnClickListener(this);
        view.findViewById(R.id.nav_button_image_thumbnail).setOnClickListener(this);
        view.findViewById(R.id.nav_button_audio).setOnClickListener(this);
        view.findViewById(R.id.nav_button_audio_album).setOnClickListener(this);
        view.findViewById(R.id.nav_button_audio_artist).setOnClickListener(this);
        view.findViewById(R.id.nav_button_audio_genre).setOnClickListener(this);
        view.findViewById(R.id.nav_button_audio_playlist).setOnClickListener(this);
        view.findViewById(R.id.nav_button_video).setOnClickListener(this);
        view.findViewById(R.id.nav_button_video_thumbnail).setOnClickListener(this);
        view.findViewById(R.id.nav_button_file).setOnClickListener(this);
        
        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        int type = -1;
        switch (id) {
            case R.id.nav_button_image:
                type = Const.MEDIA_TYPE_IMAGE;
                break;
            case R.id.nav_button_image_thumbnail:
                type = Const.MEDIA_TYPE_IMAGE_THUMBNAIL;
                break;
            case R.id.nav_button_audio:
                type = Const.MEDIA_TYPE_AUDIO;
                break;
            case R.id.nav_button_audio_album:
                type = Const.MEDIA_TYPE_AUDIO_ALBUM;
                break;
            case R.id.nav_button_audio_artist:
                type = Const.MEDIA_TYPE_AUDIO_ARTIST;
                break;
            case R.id.nav_button_audio_genre:
                type = Const.MEDIA_TYPE_AUDIO_GENRE;
                break;
            case R.id.nav_button_audio_playlist:
                type = Const.MEDIA_TYPE_AUDIO_PLAYLIST;
                break;
            case R.id.nav_button_video:
                type = Const.MEDIA_TYPE_VIDEO;
                break;
            case R.id.nav_button_video_thumbnail:
                type = Const.MEDIA_TYPE_VIDEO_THUMBNAIL;
                break;
            case R.id.nav_button_file:
                type = Const.MEDIA_TYPE_FILE;
                break;
        }
        
        Intent intent = new Intent(getActivity(), MediaStoreTableActivity.class);
        intent.putExtra(Const.MEDIA_TYPE, type);
        startActivity(intent);
    }
    
}
