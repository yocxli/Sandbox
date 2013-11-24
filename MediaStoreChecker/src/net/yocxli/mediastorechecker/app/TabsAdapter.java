package net.yocxli.mediastorechecker.app;

import java.util.ArrayList;

import net.yocxli.mediastorechecker.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

public class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    
    private static final String TAG = "TabsAdapter";
    private static final boolean LOCAL_LOGV = false;
    
    private final ActionBarActivity mActivity;
    private final ActionBar mActionBar;
    private final ViewPager mPager;
    
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    
    static final class TabInfo {
        private final Class<?> klass;
        private final Bundle   args;
        
        TabInfo(Class<?> klass, Bundle args) {
            this.klass = klass;
            this.args  = args;
        }
    }
    
    public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        
        mActivity = activity;
        
        mActionBar = activity.getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        mActionBar.setDisplayShowTitleEnabled(false);
        
        mPager = pager;
        mPager.setAdapter(this);
        mPager.setOnPageChangeListener(this);
    }
    
    public void addTab(String tabName, Class<?> klass, Bundle args) {
        TabInfo info = new TabInfo(klass, args);
        Tab tab = mActionBar.newTab().setText(tabName);
        tab.setTabListener(this);
        tab.setTag(info);
        mTabs.add(info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }
    
    public Bundle getArguments(int position) {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
        if (fragment == null) {
            TabInfo info = mTabs.get(position);
            return info != null ? info.args : null;
        } else {
            return fragment.getArguments();
        }
    }
    
    public Fragment getItemInstance(int position) {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        return fm.findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mActivity, info.klass.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

}
