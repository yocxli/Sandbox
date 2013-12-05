package net.yocxli.mediastorechecker;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;

public class MediaStoreStatusActivity extends ActionBarActivity {
    private static final String TAG = "MediaStoreStatusActivity";
    private static final boolean LOCAL_LOGV = true;
    
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_store_status);
        
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0, 0) {
//            
//        };
        
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
        // 引数のGravityはレイアウトXMLで指定しているものを指す。
        if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
            mDrawer.closeDrawer(Gravity.LEFT);
        } else {
            mDrawer.openDrawer(Gravity.LEFT);
        }
        return true;
    }

    
}
