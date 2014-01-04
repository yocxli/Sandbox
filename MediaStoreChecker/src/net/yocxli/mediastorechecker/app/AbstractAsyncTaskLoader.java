package net.yocxli.mediastorechecker.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class AbstractAsyncTaskLoader<D> extends AsyncTaskLoader<D> {
    private D mData;
    
    private volatile boolean mCanceled;
    
    public AbstractAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            return;
        }
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public boolean cancelLoad() {
        /*
         * JellyBean以降では、isLoadInBackgroundCanceled()が用意されており、
         * loadInBackgroundの中で上記メソッドをチェックすることで、処理を中断することができる。
         * サポートライブラリにはないため、自前で実装する。
         */
        mCanceled = super.cancelLoad();
        return mCanceled;
    }
    
    public boolean isLoadInBackgroundCanceled() {
        return mCanceled;
    }

    @Override
    protected void onReset() {
        mData = null;
    }

    @Override
    public abstract D loadInBackground();

}
