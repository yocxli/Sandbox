package net.yocxli.mediastorechecker;

import net.yocxli.mediastorechecker.app.MediaStoreFragment;
import net.yocxli.mediastorechecker.app.PathInputDialogFragment;
import net.yocxli.mediastorechecker.app.TabsAdapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private TabsAdapter mTabsAdapter;
    
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_tables);
        
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        
        mTabsAdapter = new TabsAdapter(this, pager);
        
        // 画像
        Bundle imageArgs = new Bundle();
        imageArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_image), MediaStoreFragment.class, imageArgs);
        
        // 画像サムネイル
        Bundle imageThumbnailArgs = new Bundle();
        imageThumbnailArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_image_thumbnail), MediaStoreFragment.class, imageThumbnailArgs);
        
        // 音楽
        Bundle audioArgs = new Bundle();
        audioArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio), MediaStoreFragment.class, audioArgs);
        
        // アルバム
        Bundle audioAlbumArgs = new Bundle();
        audioAlbumArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio_album), MediaStoreFragment.class, audioAlbumArgs);
        
        // アーティスト
        Bundle audioArtistArgs = new Bundle();
        audioArtistArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio_artist), MediaStoreFragment.class, audioArtistArgs);
        
        // ジャンル
        Bundle audioGenreArgs = new Bundle();
        audioGenreArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio_genre), MediaStoreFragment.class, audioGenreArgs);
        
        // プレイリスト
        Bundle audioPlaylistArgs = new Bundle();
        audioPlaylistArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio_playlist), MediaStoreFragment.class, audioPlaylistArgs);
        
        // 動画
        Bundle videoArgs = new Bundle();
        videoArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_video), MediaStoreFragment.class, videoArgs);
        
        // 動画サムネイル
        Bundle videoThumbnailArgs = new Bundle();
        videoThumbnailArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_video_thumbnail), MediaStoreFragment.class, videoThumbnailArgs);
        
        // ファイル
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            Bundle fileArgs = new Bundle();
            fileArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Files.getContentUri("external"));
            mTabsAdapter.addTab(getString(R.string.label_file), MediaStoreFragment.class, fileArgs);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_path_input:
                PathInputDialogFragment dialog = new PathInputDialogFragment();
                dialog.show(getSupportFragmentManager(), "path_input_dialog");
                return true;
            case R.id.action_check_provider_version:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    Toast.makeText(this, "media provider version: " + MediaStore.getVersion(this), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
