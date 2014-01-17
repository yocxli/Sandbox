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
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

/**
 * MediaStore����̃w���p�[�N���X
 * 
 * @author yocxli
 *
 */
public class MediaStoreHelper {
    private static final String TAG = "MediaStoreHelper";
    private static final boolean LOCAL_LOGV = false;
    
    /**
     * ���f�B�A�X�L�������s�����ǂ����B
     * 
     * @param cr
     * @return �X�L�������Ȃ�true�A�����łȂ����false�B
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
        cursor.close();
        if (LOCAL_LOGV) {
            Log.v(TAG, "isScanRunning: volume=" + volume);
        }
        return volume != null;
    }
    
    /**
     * MediaScannerConnection �Ńt�@�C�����X�L��������B
     * 
     * @param context
     * @param path     �X�L��������t�@�C���̃p�X�B
     * @param mimeType �X�L��������t�@�C����MIME�^�C�v�B
     * @param callback �X�L�������ʂ�ʒm����R�[���o�b�N�B�s�v�ł����null���w��B
     */
    public static void scanFile(Context context, String path, String mimeType,
                                MediaScannerConnection.OnScanCompletedListener callback) {
        scanFile(context, new String[] { path }, new String[] { mimeType }, callback);
    }
    
    /**
     * MediaScannerConnection �ŕ����̃t�@�C�����X�L��������B
     * 
     * @param context
     * @param paths     �X�L��������t�@�C���̃p�X�̔z��B
     * @param mimeTypes �X�L��������t�@�C����MIME�^�C�v�̔z��B
     * @param callback  �X�L�������ʂ�ʒm����R�[���o�b�N�B�s�v�ł����null���w��B
     */
    public static void scanFile(Context context, String[] paths, String[] mimeTypes,
                                MediaScannerConnection.OnScanCompletedListener callback) {
        
        MediaScannerConnection.scanFile(context, paths, mimeTypes, callback);
    }
    
    /**
     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE �Ńt�@�C�����X�L��������B
     * 
     * @param context
     * @param path �X�L��������t�@�C���̃p�X�B
     */
    public static void scanFileByIntent(Context context, String path) {
        scanFileByIntent(context, new File(path));
    }
    
    /**
     * Intent.ACTION_MEDIA_SCANNER_SCAN_FILE �Ńt�@�C�����X�L��������B
     * 
     * @param context
     * @param file �X�L��������t�@�C���B
     */
    public static void scanFileByIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
    
    /**
     * MediaStore ����t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param path �폜����t�@�C���̃p�X�B
     */
    public static void deleteFile(ContentResolver cr, String path) {
        deleteFile(cr, new String[] { path });
    }
    
    /**
     * MediaStore ���畡���̃t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param paths �폜����t�@�C���̃p�X�̔z��B
     */
    @SuppressLint("NewApi")
    public static void deleteFile(ContentResolver cr, String[] paths) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "[deleteFile]");
        }
        
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            final String where = MediaStore.MediaColumns.DATA + " = ?";
            final int num = paths.length;
            for (int i = 0; i < num; i++) {
                String[] args = new String[]{ paths[i] };
                
                int result = cr.delete(MediaStore.Files.getContentUri("external"), where, args);
                if (LOCAL_LOGV) {
                    Log.v(TAG, "[deleteFile] delete result: " + result);
                }
            }
        } else {
            deleteFileFromMedia(cr, paths);
        }
    }
    
    /**
     * MediaStore ����f�B���N�g���̃��R�[�h�Ƃ��̃f�B���N�g���Ɋ܂܂��t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param directory �폜����f�B���N�g���B
     */
    public static void deleteDirectory(ContentResolver cr, String directory) {
        deleteDirectory(cr, new String[] { directory });
    }
    
    /**
     * MediaStore ���畡���̃f�B���N�g���̃��R�[�h�Ƃ��ꂼ��̃f�B���N�g���Ɋ܂܂��t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param directories �폜����f�B���N�g���̔z��B
     */
    @SuppressLint("NewApi")
    public static void deleteDirectory(ContentResolver cr, String[] directories) {
        if (LOCAL_LOGV) {
            Log.v(TAG, "[deleteDirectory]");
        }
        
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            final String where = MediaStore.MediaColumns.DATA + " LIKE ?";
            final int num = directories.length;
            for (int i = 0; i < num; i++) {
                String directory = directories[i];
                if (!directory.endsWith(File.separator)) {
                    directory += File.separator;
                }
                String[] args = new String[]{ directory + "%" };
                
                int result = cr.delete(MediaStore.Files.getContentUri("external"), where, args);
                if (LOCAL_LOGV) {
                    Log.v(TAG, "[deleteDirectory] delete result: " + result);
                }
            }
        } else {
            deleteDirectoryFromMedia(cr, directories);
        }
    }
    
    /**
     * MediaStore �̃��f�B�A�n�e�[�u������t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param path �폜����t�@�C���̃p�X�B
     */
    public static void deleteFileFromMedia(ContentResolver cr, String path) {
        deleteFileFromMedia(cr, new String[] { path });
    }
    
    /**
     * MediaStore �̃��f�B�A�n�e�[�u�����畡���̃t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param paths �폜����t�@�C���̃p�X�̔z��B
     */
    public static void deleteFileFromMedia(ContentResolver cr, String[] paths) {
        final String where = MediaStore.MediaColumns.DATA + " = ?";
        final int num = paths.length;
        for (int i = 0; i < num; i++) {
            String[] args = new String[] { paths[i] };
            final int imageResult = cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, args);
            final int audioResult = cr.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, where, args);
            final int videoResult = cr.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, where, args);
            if (LOCAL_LOGV) {
                Log.v(TAG, "[deleteFileFromMedia] delete result: image=" + imageResult + ", audio=" + audioResult + ", video=" + videoResult);
            }
        }
    }
    
    /**
     * MediaStore �̃��f�B�A�n�e�[�u������C�ӂ̃f�B���N�g���ȉ��̃t�@�C���̃��R�[�h���폜����B
     * 
     * @param cr
     * @param direcotry �폜�Ώۂ̃f�B���N�g���B
     */
    public static void deleteDirectoryFromMedia(ContentResolver cr, String direcotry) {
        deleteDirectoryFromMedia(cr, new String[] { direcotry });
    }
    
    /**
     * MediaStore �̃��f�B�A�n�e�[�u������C�ӂ̕����̃f�B���N�g���ȉ��̃t�@�C���̃��R�[�h���폜����B
     * @param cr
     * @param directories �폜�Ώۂ̃f�B���N�g���̔z��B
     */
    public static void deleteDirectoryFromMedia(ContentResolver cr, String[] directories) {
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
            if (LOCAL_LOGV) {
                Log.v(TAG, "[deleteDirectoryFromMedia] delete result: image=" + imageResult + ", audio=" + audioResult + ", video=" + videoResult);
            }
        }
    }
    
    /**
     * �^����ꂽ File �� content URI ��Ԃ��B
     * @param cr
     * @param file
     * @return
     */
    public static Uri getUriForFile(ContentResolver cr, File file) {
        Uri uri = null;
        // TODO: ��������
        return uri;
    }

    /**
     * �^����ꂽ content URI �̃t�@�C���p�X��Ԃ��B
     * @param cr
     * @param uri
     * @return
     */
    public static String getFilePathForUri(ContentResolver cr, Uri uri) {
        String path = null;
        Cursor c = cr.query(uri, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                final int index = c.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (index == -1) {
                    return null;
                }
                path = c.getString(index);
            }
            c.close();
        }
        return path;
    }
    
    /**
     * �^����ꂽ content URI �� File ��Ԃ��B
     * @param cr
     * @param uri
     * @return
     */
    public static File getFileForUri(ContentResolver cr, Uri uri) {
        String path = getFilePathForUri(cr, uri);
        return path == null ? null : new File(path);
    }
    
    public static boolean isImageUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Images.Media.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Images.Media.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isImageThumbnailUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Images.Thumbnails.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Images.Thumbnails.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isAudioUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isAudioAlbumUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Audio.Albums.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Audio.Albums.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isAudioArtistUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Audio.Artists.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Audio.Artists.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isAudioGenreUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Audio.Genres.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Audio.Genres.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isAudioPlaylistUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Audio.Playlists.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Audio.Playlists.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isVideoUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isVideoThumbnailUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Video.Thumbnails.EXTERNAL_CONTENT_URI.toString())
                || uriStr.startsWith(Video.Thumbnails.INTERNAL_CONTENT_URI.toString());
    }
    
    public static boolean isFileUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        String uriStr = uri.toString();
        return uriStr.startsWith(Files.getContentUri("external").toString())
                || uriStr.startsWith(Files.getContentUri("internal").toString());
    }
    
    public static boolean hasFileTable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
 }
