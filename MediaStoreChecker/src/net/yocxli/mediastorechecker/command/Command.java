package net.yocxli.mediastorechecker.command;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

public class Command {
    /**
     * ファイルアクション用のバックグランド処理をするクラス。
     */
    protected static class CommandTask extends AsyncTask<CommandTask, CommandEvent, CommandEvent> {
        private Context mContext;
        private CommandProgram mProgram;
        
        public CommandTask(Context context, CommandProgram program) {
            mContext = context;
            mProgram = program;
        }

        @Override
        protected CommandEvent doInBackground(CommandTask... params) {
            return mProgram.execute(params[0]);
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @SuppressLint("NewApi")
        @Override
        protected void onCancelled(CommandEvent result) {
            // TODO Auto-generated method stub
            super.onCancelled(result);
        }

        @Override
        protected void onPostExecute(CommandEvent result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(CommandEvent... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
        
    }
    
    public interface CommandProgram {
        CommandEvent execute(CommandTask task);
        
        /**
         * プログレスダイアログに表示する要素を生成する
         */
        void requestProgress();
    }
    
    protected static class CommandEvent {
        public static final int RESULT_OK = 1;
        
        public long totalSize;
        public long doneSize;
        
        public int totalItemCount;
        public int count;
    }
    
    public interface ExecutionCallback {
        public void onProgress(CommandEvent event);
    }
    
    public interface ExecutionCanceller {
        public boolean isCancelled();
    }
    
    public static void executeInBackground(Context context, CommandProgram program) {
        CommandTask task = new CommandTask(context, program);
        task.execute(task);
    }
    
}
