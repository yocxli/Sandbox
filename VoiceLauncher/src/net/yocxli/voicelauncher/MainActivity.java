package net.yocxli.voicelauncher;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, RecognitionListener {
    private static final String TAG = "MainActivity";
    
    private SpeechRecognizer mRecognizer;
    private TextView mText;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        
        Button start = (Button) findViewById(R.id.button_start);
        start.setOnClickListener(this);
        
        Button stop = (Button) findViewById(R.id.button_stop);
        stop.setOnClickListener(this);
        
        mText = (TextView) findViewById(R.id.text);
        mResult = (TextView) findViewById(R.id.result);
        
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(this);
    }
    
    

    @Override
    protected void onStop() {
        super.onStop();
        mRecognizer.destroy();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.v(TAG, "onBeginningOfSpeech");
        mText.setText("beggining of speech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.v(TAG, "onEndOfSpeech");
        mText.setText("end of speech");
    }

    @Override
    public void onError(int error) {
        Log.v(TAG, "onError");
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
//            Toast.makeText(this, "no match Text data", Toast.LENGTH_LONG).show();
            Log.e(TAG, "no match");
            break;
        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
            // RecognitionServiceへ要求出せず
            Log.e(TAG, "recognizer busy");
            mRecognizer.cancel();
            break;
        case SpeechRecognizer.ERROR_SERVER:
            // Server側からエラー通知
            Log.e(TAG, "server error");
            break;
        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
            // 音声入力無し
//            Toast.makeText(this, "no input?", Toast.LENGTH_LONG).show();
            Log.e(TAG, "time out");
            break;
        default:
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
        mText.setText("ready for speech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.v(TAG, "onResults");
        ArrayList<String> recData = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
     
        boolean match = false;
        String getData = new String();
        for (String s : recData) {
            if (!match) {
                match = mLists.contains(s);
            }
            getData += s + ",";
        }
        mResult.setText(getData);
        if (!match) {
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    startListening();
                }
                
            });
        } else {
            launchActivity();
        }
    }
    
    static ArrayList<String> mLists = new ArrayList<String>();
    
    static {
        mLists.add("音無さん");
        mLists.add("音無山");
        mLists.add("音は資産");
        mLists.add("下足山");
        mLists.add("本橋さん");
        mLists.add("音足山");
        mLists.add("おと足山");
        mLists.add("音話");
        
        mLists.add("テスト");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
//        Log.v(TAG, "onRmsChanged: rmsdB=" + rmsdB);
    }

    @Override
    public void onClick(View v) {
        Log.v(TAG, "onClick");
        switch (v.getId()) {
            case R.id.button:
                startListening();
                break;
            case R.id.button_start:
                startService(new Intent(this, LauncherService.class));
                break;
            case R.id.button_stop:
                stopService(new Intent(this, LauncherService.class));
                break;
        }
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        mRecognizer.startListening(intent);
    }
    
    private void launchActivity() {
        Intent intent = new Intent("jp.co.yahoo.android.vassist.action.TALK");
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setClassName("jp.co.yahoo.android.vassist", "jp.co.yahoo.android.yfiler.YFHandleActivity");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }
    }
}
