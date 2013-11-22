package net.yocxli.voicelauncher;

import java.util.ArrayList;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class LauncherService extends Service implements RecognitionListener {
    private static final String TAG = "LauncherService";
    
    private static final int NOTIFICATION_ID = 3147;
    
    private NotificationManager mNotificationManager;
    
    private SpeechRecognizer mRecognizer;
    
    private Handler mHandler;
    
    private boolean mIsListening;
    
    private boolean mIsMatched;
    private BroadcastReceiver mReceiver;
    
    private long mLastTimeStartListening;
    private long mLastTimeReadyForSpeech;
    private long mLastTimeRmsChanged;
    private int mSequenceRmsCount;
    
    private static ArrayList<String> mLists = new ArrayList<String>();
    
    static {
        mLists.add("音無さん");
        mLists.add("音無山");
        mLists.add("音は資産");
        mLists.add("下足山");
        mLists.add("本橋さん");
        mLists.add("音足山");
        mLists.add("おと足山");
        mLists.add("音話");
        mLists.add("音無");
        mLists.add("下足");
        mLists.add("本橋");
        mLists.add("音羽山");
        mLists.add("音羽");
        mLists.add("お父さん");
        mLists.add("お母さん");
        
        mLists.add("てすと");
        mLists.add("テスト");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(this);
        
        mHandler = new Handler();
        mIsMatched = false;
        
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mIsMatched = false;
                startListening();
            }
            
        };
        registerReceiver(mReceiver, new IntentFilter("jp.co.yahoo.android.vassist.action.WAIT_CHAT"));
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager.notify(
                NOTIFICATION_ID, createNotification(R.drawable.ic_launcher, "Start"));
        startListening();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mNotificationManager.cancel(NOTIFICATION_ID);
        mHandler.removeCallbacks(checkRunnable);
        mRecognizer.cancel();
        mRecognizer.destroy();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.v(TAG, "onBeginningOfSpeech");
        mIsListening = true;
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.v(TAG, "onEndOfSpeech");
        mIsListening = false;
    }

    @Override
    public void onError(int error) {
        Log.v(TAG, "onError");
        mNotificationManager.notify(NOTIFICATION_ID, createNotification(android.R.drawable.stat_notify_error, "Error"));
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                Log.e(TAG, "audio error");
                // 音声データ保存失敗
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                // Android端末内のエラー(その他)
                Log.e(TAG, "client error");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                // 権限無し
                Log.e(TAG, "permission error");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                // ネットワークエラー(その他)
                Log.e(TAG, "network error");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                // ネットワークタイムアウトエラー
                Log.e(TAG, "network timeout");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                // 音声認識結果無し
//                Toast.makeText(this, "no match Text data", Toast.LENGTH_LONG).show();
                Log.e(TAG, "no match");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                // RecognitionServiceへ要求出せず
                mRecognizer.cancel();
                Log.e(TAG, "recognizer busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                // Server側からエラー通知
                Log.e(TAG, "server error");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                // 音声入力無し
//                Toast.makeText(this, "no input?", Toast.LENGTH_LONG).show();
                Log.e(TAG, "time out");
                break;
            default:
        }
        if (!mIsMatched) {
            restartListening();
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.v(TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.v(TAG, "onReadyForSpeech");
//        Toast.makeText(this, TAG + ".onReadyForSpeech", Toast.LENGTH_SHORT).show();
        mNotificationManager.notify(
                NOTIFICATION_ID, createNotification(android.R.drawable.stat_notify_chat, "Ready", 0xff00ff00));
        mIsListening = false;
        mHandler.removeCallbacks(checkRunnable);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResults(Bundle results) {
        Log.v(TAG, "onResults");
        ArrayList<String> recData = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
     
        boolean match = false;
        StringBuffer buf = new StringBuffer();
        for (String s : recData) {
            buf.append("[" + s + "]");
            if (!match) {
                match = mLists.contains(s);
            }
        }
        Log.v(TAG, "words: " + buf.toString());
        if (!match) {
            mNotificationManager.notify(NOTIFICATION_ID, createNotification(R.drawable.ic_launcher, "Relisten"));
            restartListening();
        } else {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            @SuppressWarnings("deprecation")
            PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "Your App Tag");
            wakelock.acquire();
            wakelock.release();
            KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock klock = km.newKeyguardLock("Your App Tag");
            klock.disableKeyguard();
            mIsMatched = true;
            mNotificationManager.notify(NOTIFICATION_ID, createNotification(R.drawable.ic_launcher, "End"));
            launchActivity();
//            stopListening();
        }
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        long current = System.currentTimeMillis();
        if ((current - mLastTimeRmsChanged) > 1000) {
            Log.v(TAG, "onRmsChanged: rmsdB=" + rmsdB);
            mLastTimeRmsChanged = current;
            mSequenceRmsCount++;
        }
        if (mSequenceRmsCount > 7) {
            Log.v(TAG, "rms count is over: " + mSequenceRmsCount);
            restartListening();
        }
    }
    
    private Runnable checkRunnable = new Runnable() {

        @Override
        public void run() {
           Log.v(TAG, "force restarting");
           startListening();
        }
        
    };
    
    private boolean runnablePrepared = false;
    
    private void startListening() {
        Log.v(TAG, "startListening");
        mRecognizer.cancel();
        mLastTimeRmsChanged = 0;
        mSequenceRmsCount = 0;
        mLastTimeStartListening = System.currentTimeMillis();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        mRecognizer.startListening(intent);
        
        if (!runnablePrepared) {
            mHandler.postDelayed(checkRunnable, 5000);
            runnablePrepared = true;
        }
    }
    
    private void restartListening() {
        Log.v(TAG, "restartListening");
//        mHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
                startListening();
//            }
//            
//        });
    }
    
    private void stopListening() {
        Log.v(TAG, "stopListening");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Log.v(TAG, "stopListening");
                mRecognizer.stopListening();
            }
            
        });
    }
    
    private void launchActivity() {
        Intent intent = new Intent("jp.co.yahoo.android.vassist.action.TALK");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setClassName("jp.co.yahoo.android.vassist", "jp.co.yahoo.android.yfiler.YFHandleActivity");
        try {
            startActivity(intent);
            mRecognizer.stopListening();
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Notification createNotification(int iconRes, String title) {
        return createNotification(iconRes, title, 0);
    }

    private Notification createNotification(int iconRes, String title, int led) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(iconRes);
        builder.setContentTitle(title);
        if (led != 0) {
            Log.v(TAG, "led set =" + led);
            builder.setLights(led, 1, 0);
        }
        return builder.build();
    }
}
