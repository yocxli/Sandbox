package net.yocxli.mediastorechecker;

import java.io.File;

import net.yocxli.mediastorechecker.app.MediaStoreDetailActivity;
import net.yocxli.mediastorechecker.app.MediaStoreFragment;
import net.yocxli.mediastorechecker.app.PathInputDialogFragment;
import net.yocxli.mediastorechecker.app.TabsAdapter;
import net.yocxli.mediastorechecker.util.MediaStoreHelper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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
        
        Toast.makeText(this, "media provider version: " + MediaStore.getVersion(this), Toast.LENGTH_SHORT).show();
        
        final FragmentManager fm = getSupportFragmentManager();
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        
        mTabsAdapter = new TabsAdapter(this, pager);
        
        Bundle imageArgs = new Bundle();
        imageArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_image), MediaStoreFragment.class, imageArgs);
        
        Bundle audioArgs = new Bundle();
        audioArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_audio), MediaStoreFragment.class, audioArgs);
        
        Bundle videoArgs = new Bundle();
        videoArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        mTabsAdapter.addTab(getString(R.string.label_video), MediaStoreFragment.class, videoArgs);
        
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            Bundle fileArgs = new Bundle();
            fileArgs.putParcelable(MediaStoreFragment.TARGET_URI, MediaStore.Files.getContentUri("external"));
            mTabsAdapter.addTab(getString(R.string.label_file), MediaStoreFragment.class, fileArgs);
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_path_input:
                PathInputDialogFragment dialog = new PathInputDialogFragment();
                dialog.show(getSupportFragmentManager(), "path_input_dialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
