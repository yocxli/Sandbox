package net.yocxli.mediastorechecker;

import net.yocxli.mediastorechecker.app.DrawerFragment;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;

public class MediaStoreStatusActivity extends ActionBarActivity {
    private static final String TAG = "MediaStoreStatusActivity";
    private static final boolean LOCAL_LOGV = true;
    
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.os.Debug.waitForDebugger();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_store_status);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            TextView version = (TextView) findViewById(R.id.media_store_version);
            version.setText(MediaStore.getVersion(this));
        }
        
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0, 0) {
//        };
        ((DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer)).setOnNavigationClickListener(new DrawerFragment.OnNavigationClickListener() {

            @Override
            public void onNavigationClick() {
                toggleDrawer();
            }
            
        });
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_store_status, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (LOCAL_LOGV) {
            Log.v(TAG, "onSupportNavigateUp");
        }
        toggleDrawer();
        return true;
    }

    private void toggleDrawer() {
        // 引数のGravityはレイアウトXMLで指定しているものを指す。
        if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
            mDrawer.closeDrawer(Gravity.LEFT);
        } else {
            mDrawer.openDrawer(Gravity.LEFT);
        }
    }
}
