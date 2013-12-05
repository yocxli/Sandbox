package net.yocxli.mediastorechecker.app;

import java.io.File;
import java.io.IOException;

import net.yocxli.mediastorechecker.MediaScanReceiver;
import net.yocxli.mediastorechecker.R;
import net.yocxli.mediastorechecker.util.MediaStoreHelper;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MediaStoreDetailActivity extends ActionBarActivity {
    private static final String TAG = "MediaStoreDetailActivity";
    private static final boolean LOCAL_LOGV = true;
    
    public static final String EXTRA_ID = "id";
    
    private TabsAdapter mTabsAdapter;
    
    private Uri mData;
    private String mFilePath;
    
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData = getIntent().getData();
        Bundle extra = getIntent().getExtras();
        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && extra != null) {
            // ‹¤—L
            mData = (Uri) extra.getParcelable(Intent.EXTRA_STREAM);
            if (LOCAL_LOGV) {
                Log.v(TAG, "[onCreate] sent uri=" + mData.toString());
            }
        }
        mFilePath = MediaStoreHelper.getFilePathForUri(getContentResolver(), mData);
        
        buildTabs();
    }
    
    @SuppressLint("NewApi")
    private void buildTabs() {
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, pager);
        final String uriStr = mData.toString();
        if (uriStr.startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())) {
            Bundle imageArgs = new Bundle();
            imageArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.label_image), MediaStoreDetailFragment.class, imageArgs);
        } else if (uriStr.startsWith(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.toString())) {
            Bundle imageThumbArgs = new Bundle();
            imageThumbArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.label_image_thumbnail), MediaStoreDetailFragment.class, imageThumbArgs);
        } else if (uriStr.startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
            Bundle audioArgs = new Bundle();
            audioArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.label_audio), MediaStoreDetailFragment.class, audioArgs);
        } else if (uriStr.startsWith(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI.toString())) {
            Bundle audioAlbumArgs = new Bundle();
            audioAlbumArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.label_audio_album), MediaStoreDetailFragment.class, audioAlbumArgs);
        } else if (uriStr.startsWith(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.toString())) {
        } else if (uriStr.startsWith(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI.toString())) {
        } else if (uriStr.startsWith(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI.toString())) {
        } else if (uriStr.startsWith(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString())) {
            Bundle videoArgs = new Bundle();
            videoArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            mTabsAdapter.addTab(getString(R.string.label_video), MediaStoreDetailFragment.class, videoArgs);
        } else if (uriStr.startsWith(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI.toString())) {
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && 
                   uriStr.startsWith(MediaStore.Files.getContentUri("external").toString())) {
            Bundle fileArgs = new Bundle();
            fileArgs.putParcelable(MediaStoreDetailFragment.TARGET_URI, MediaStore.Files.getContentUri("external"));
            mTabsAdapter.addTab(getString(R.string.label_file), MediaStoreDetailFragment.class, fileArgs);
        }
    }
    
    private void update() {
//        int num = mTabsAdapter.getCount();
//        for (int i = 0; i < num; i++) {
//            Fragment fragment = mTabsAdapter.getItemInstance(i);
//            if (fragment instanceof MediaStoreDetailFragment) {
//                ((MediaStoreDetailFragment) fragment).update();
//            } else if (fragment instanceof FileInfoFragment) {
//                ((FileInfoFragment) fragment).update();
//            }
//        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String path = mData.getPath();
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, R.string.message_empty_path, Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_delete: {
                File file = new File(path);
                file.delete();
                update();
                return true;
            }
            case R.id.action_create_file: {
                File file = new File(path);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.message_failure_create_file, Toast.LENGTH_SHORT).show();
                }
                update();
                return true;
            }
            case R.id.action_create_directory: {
                File file = new File(path);
                boolean result = file.mkdirs();
                if (!result) {
                    Toast.makeText(this, R.string.message_failure_create_directory, Toast.LENGTH_SHORT).show();
                }
                update();
                return true;
            }
            case R.id.action_scan_file:
                final Resources res = getResources();
                String title = res.getString(R.string.notification_scan_started);
                Notification notification = MediaScanReceiver.createNotification(this, title, path);
                MediaScanReceiver.updateNotification(this, notification);
                MediaStoreHelper.scanFile(this, path, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        String title = res.getString(R.string.notification_scan_completed);
                        Notification notification = MediaScanReceiver.createNotification(MediaStoreDetailActivity.this, title, path);
                        MediaScanReceiver.updateNotification(MediaStoreDetailActivity.this, notification);
                        update();
                    }
                });
                return true;
            case R.id.action_scan_file_intent:
                MediaStoreHelper.scanFileByIntent(this, new File(path));
                update();
                return true;
            case R.id.action_delete_record_media:
                MediaStoreHelper.deleteFileFromMedia(getContentResolver(), path);
                update();
                return true;
            case R.id.action_delete_record_file:
                MediaStoreHelper.deleteFile(getContentResolver(), path);
                update();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
