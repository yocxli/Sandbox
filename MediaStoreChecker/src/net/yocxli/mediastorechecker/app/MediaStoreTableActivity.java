package net.yocxli.mediastorechecker.app;

import net.yocxli.mediastorechecker.Const;
import net.yocxli.mediastorechecker.R;
import net.yocxli.mediastorechecker.R.id;
import net.yocxli.mediastorechecker.R.layout;
import net.yocxli.mediastorechecker.R.menu;
import net.yocxli.mediastorechecker.R.string;
import net.yocxli.mediastorechecker.util.MediaStoreHelper;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MediaStoreTableActivity extends ActionBarActivity {

    private TabsAdapter mTabsAdapter;
    private int mType;
    
    private DrawerLayout mDrawer;
    
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mType = getIntent().getIntExtra(Const.MEDIA_TYPE, -1);
        String typeText = null;
        
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        
        mTabsAdapter = new TabsAdapter(this, pager);
        
        if (mType == Const.MEDIA_TYPE_IMAGE) {
            // 画像
            typeText = getString(R.string.label_image);
            
            Bundle imageExternalArgs = new Bundle();
            imageExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, imageExternalArgs);
            
            Bundle imageInternalArgs = new Bundle();
            imageInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, imageInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_IMAGE_THUMBNAIL) {
            // 画像サムネイル
            typeText = getString(R.string.label_image_thumbnail);
            
            Bundle imageExternalThumbnailArgs = new Bundle();
            imageExternalThumbnailArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, imageExternalThumbnailArgs);
            
            Bundle imageInternalThumbnailArgs = new Bundle();
            imageInternalThumbnailArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, imageInternalThumbnailArgs);
        } else if (mType == Const.MEDIA_TYPE_AUDIO) {
            // 音楽
            typeText = getString(R.string.label_audio);
            
            Bundle audioExternalArgs = new Bundle();
            audioExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, audioExternalArgs);
            
            Bundle audioInternalArgs = new Bundle();
            audioInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, audioInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_AUDIO_ALBUM) {
            // アルバム
            typeText = getString(R.string.label_audio_album);
            
            Bundle audioAlbumExternalArgs = new Bundle();
            audioAlbumExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, audioAlbumExternalArgs);
            
            Bundle audioAlbumInternalArgs = new Bundle();
            audioAlbumInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Albums.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, audioAlbumInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_AUDIO_ARTIST) {
            // アーティスト
            typeText = getString(R.string.label_audio_artist);
            
            Bundle audioArtistExternalArgs = new Bundle();
            audioArtistExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, audioArtistExternalArgs);
            
            Bundle audioArtistInternalArgs = new Bundle();
            audioArtistInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Artists.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, audioArtistInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_AUDIO_GENRE) {
            // ジャンル
            typeText = getString(R.string.label_audio_genre);
            
            Bundle audioGenreExternalArgs = new Bundle();
            audioGenreExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, audioGenreExternalArgs);
            
            /* no such table で落ちる（Xperia AX）
            Bundle audioGenreInternalArgs = new Bundle();
            audioGenreInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Genres.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, audioGenreInternalArgs);
            */
        } else if (mType == Const.MEDIA_TYPE_AUDIO_PLAYLIST) {
            // プレイリスト
            typeText = getString(R.string.label_audio_playlist);
            
            Bundle audioPlaylistExternalArgs = new Bundle();
            audioPlaylistExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, audioPlaylistExternalArgs);
            
            /* no such table で落ちる (Xperia AX)
            Bundle audioPlaylistInternalArgs = new Bundle();
            audioPlaylistInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, audioPlaylistInternalArgs);
            */
        } else if (mType == Const.MEDIA_TYPE_VIDEO) {
            // 動画
            typeText = getString(R.string.label_video);
            
            Bundle videoExternalArgs = new Bundle();
            videoExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, videoExternalArgs);
            
            Bundle videoInternalArgs = new Bundle();
            videoInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, videoInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_VIDEO_THUMBNAIL) {
            // 動画サムネイル
            typeText = getString(R.string.label_video_thumbnail);
            
            Bundle videoThumbnailExternalArgs = new Bundle();
            videoThumbnailExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, videoThumbnailExternalArgs);
            
            Bundle videoThumbnailInternalArgs = new Bundle();
            videoThumbnailInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, videoThumbnailInternalArgs);
        } else if (mType == Const.MEDIA_TYPE_FILE) {
            // ファイル
            typeText = getString(R.string.label_file);
            
            if (MediaStoreHelper.hasFileTable()) {
                Bundle fileExternalArgs = new Bundle();
                fileExternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Files.getContentUri("external"));
                mTabsAdapter.addTab(getString(R.string.volume_external), MediaStoreFragment.class, fileExternalArgs);
                
                Bundle fileInternalArgs = new Bundle();
                fileInternalArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Files.getContentUri("internal"));
                mTabsAdapter.addTab(getString(R.string.volume_internal), MediaStoreFragment.class, fileInternalArgs);
            }
        }
        
        final ActionBar actionBar = getSupportActionBar();
        String title = String.format(getString(R.string.title_tables_format), typeText);
        actionBar.setTitle(title);
        
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        // 引数のGravityはレイアウトXMLで指定しているものを指す。
        if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
            mDrawer.closeDrawer(Gravity.LEFT);
        } else {
            mDrawer.openDrawer(Gravity.LEFT);
        }
        return true;
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
