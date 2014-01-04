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
         * JellyBean�ȍ~�ł́AisLoadInBackgroundCanceled()���p�ӂ���Ă���A
         * loadInBackground�̒��ŏ�L���\�b�h���`�F�b�N���邱�ƂŁA�����𒆒f���邱�Ƃ��ł���B
         * �T�|�[�g���C�u�����ɂ͂Ȃ����߁A���O�Ŏ�������B
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
