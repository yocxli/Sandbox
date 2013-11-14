package net.yocxli.mediastorechecker.command.local;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

import net.yocxli.mediastorechecker.command.Command;
import net.yocxli.mediastorechecker.command.Command.ExecutionCallback;
import net.yocxli.mediastorechecker.command.Command.ExecutionCanceller;

public class CommandHelper {
    private static final String TAG = "LocalCommandHelper";
    
    public static void delete(File file, ExecutionCallback callback, ExecutionCanceller canceller) {
        if (canceller != null && canceller.isCancelled()) {
            return;
        }
        if (callback != null) {
        }
        if (file.isDirectory()) {
            File[] lists = file.listFiles();
            final int num = lists.length;
            for (int i = 0; i < num; i++) {
                delete(lists[i], callback, canceller);
            }
        } else {
            file.delete();
        }
    }
    
    
    /**
     * コピーする。
     *
     * @param src The source file
     * @param dst The destination file
     * @param bufferSize The buffer size for the operation
     * @return boolean If the operation complete successfully
     */
    public static boolean copy(final File src, final File dst, int bufferSize,
            ExecutionCallback callback, ExecutionCanceller canceller) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src), bufferSize);
            bos = new BufferedOutputStream(new FileOutputStream(dst), bufferSize);
            int read = 0;
            byte[] data = new byte[bufferSize];
            while ((read = bis.read(data, 0, bufferSize)) != -1) {
                if (canceller != null && canceller.isCancelled()) {
                    break;
                }
                bos.write(data, 0, read);
            }
            return true;
        } catch (Throwable e) {
            Log.e(TAG, String.format(TAG, "Failed to copy from %s to %d", src, dst), e);
            return false;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                // 無視する。
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                // 無視する。
            }
        }
    }
}
