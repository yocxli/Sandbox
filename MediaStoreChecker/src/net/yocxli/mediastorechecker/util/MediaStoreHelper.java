package net.yocxli.mediastorechecker.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

public class MediaStoreHelper {
    private static final String TAG = "MediaStoreHelper";
    private static final boolean LOCAL_LOGV = false;
    
    /**
     * メディアスキャン実行中かどうか。
     * 
     * @param cr
     * @return
     */
    public static boolean isScanRunning(ContentResolver cr) {
        Cursor cursor = cr.query(MediaStore.getMediaScannerUri(),
                                 new String[]{ MediaStore.MEDIA_SCANNER_VOLUME },
                                 null, null, null);
        if (cursor == null) {
            return false;
        }
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.MEDIA_SCANNER_VOLUME);
        String volume = cursor.getString(index);
        Log.v(TAG, "isScanRunning: volume=" + volume);
        cursor.close();
        return volume != null;
    }
    
    /**
     * MediaScannerConnection でファイルをスキャンする。
     * 
     * @param context
     * @param path
     * @param mimeType
     * @param callback
     */
    public static void scanFile(Context context, String path, String mimeType,
                                MediaScannerConnection.OnScanCompletedListener callback) {
        scanFile(context, new String[] { path }, new String[] { mimeType }, callback);
    }
    
    /**
     * MediaScannerConnection でファイルをスキャンする。
     * 
     * @param context
     * @param paths
     * @param mimeTypes
     * @param callback
     */
    public static void scanFile(Context context, String[] paths, String[] mimeTypes,
                                MediaScannerConnection.OnScanCompletedListener callback) {
        
        MediaScannerConnection.scanFile(context, paths, mimeTypes, callback);
    }
    
    /**
     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE でファイルをスキャンする。
     * 
     * @param context
     * @param path
     */
    public static void scanFileByIntent(Context context, String path) {
        scanFileByIntent(context, new File(path));
    }
    
    /**
     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE でファイルをスキャンする。
     * 
     * @param context
     * @param file
     */
    public static void scanFileByIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
    
    public static void deleteFile(Context context, String path) {
        deleteFile(context, new String[] { path });
    }
    
    @SuppressLint("NewApi")
    public static void deleteFile(Context context, String[] paths) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "deleteFile");
        }
        
        ContentResolver cr = context.getContentResolver();
        final String where = MediaStore.MediaColumns.DATA + " = ?";
        final int num = paths.length;
        for (int i = 0; i < num; i++) {
            String[] args = new String[]{ paths[i] };
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                int result = cr.delete(MediaStore.Files.getContentUri("external"), where, args);
                if (LOCAL_LOGV) {
                    Log.d(TAG, "delete result: " + result);
                }
            }
        }
    }
    
    public static void deleteDirectory(Context context, String directory) {
        deleteDirectory(context, new String[] { directory });
    }
    
    @SuppressLint("NewApi")
    public static void deleteDirectory(Context context, String[] directories) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "deleteDirectory");
        }
        
        ContentResolver cr = context.getContentResolver();
        final String where = MediaStore.MediaColumns.DATA + " LIKE ?";
        final int num = directories.length;
        for (int i = 0; i < num; i++) {
            String directory = directories[i];
            if (!directory.endsWith(File.separator)) {
                directory += File.separator;
            }
            String[] args = new String[]{ directory + "%" };
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                int result = cr.delete(MediaStore.Files.getContentUri("external"), where, args);
                if (LOCAL_LOGV) {
                    Log.d(TAG, "delete result: " + result);
                }
            }
        }
    }
    
    public static void deleteFileFromMedia(Context context, String path) {
        deleteFileFromMedia(context, new String[] { path });
    }
    
    public static void deleteFileFromMedia(Context context, String[] paths) {
        ContentResolver cr = context.getContentResolver();
        final String where = MediaStore.MediaColumns.DATA + " LIKE ?";
        final int num = paths.length;
        for (int i = 0; i < num; i++) {
            String[] args = new String[] { paths[i] };
            int imageResult = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, args);
            int audioResult = cr.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, where, args);
            int videoResult = cr.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, where, args);
            Log.d(TAG, "delete result: image=" + imageResult + ", audio=" + audioResult + ", video=" + videoResult);
        }
    }
    
    public static void deleteDirectoryFromMedia(Context context, String direcotry) {
        deleteFileFromMedia(context, new String[] { direcotry });
    }
    
    public static void deleteDirectoryFromMedia(Context context, String[] directories) {
        ContentResolver cr = context.getContentResolver();
        final String where = MediaStore.MediaColumns.DATA + " LIKE ?";
        final int num = directories.length;
        for (int i = 0; i < num; i++) {
            String directory = directories[i];
            if (!directory.endsWith(File.separator)) {
                directory += File.separator;
            }
            String[] args = new String[] { directory + "%" };
            int imageResult = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, args);
            int audioResult = cr.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, where, args);
            int videoResult = cr.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, where, args);
            Log.d(TAG, "delete result: image=" + imageResult + ", audio=" + audioResult + ", video=" + videoResult);
        }
    }

    public static String getFilePath(ContentResolver cr, Uri uri) {
        String path = null;
        Cursor c = cr.query(uri, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            final int index = c.getColumnIndex(MediaStore.MediaColumns.DATA);
            path = c.getString(index);
            c.close();
        }
        return path;
    }
}
