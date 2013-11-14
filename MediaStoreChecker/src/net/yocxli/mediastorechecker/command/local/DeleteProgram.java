package net.yocxli.mediastorechecker.command.local;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import net.yocxli.mediastorechecker.command.Command;


public class DeleteProgram extends Command implements Command.CommandProgram {
    private ArrayList<File> mFiles;
    
    public DeleteProgram(File file) {
        ArrayList<File> files = new ArrayList<File>(1);
        files.add(file);
        initialize(files);
    }
    
    public DeleteProgram(File[] files) {
        initialize(new ArrayList<File>(Arrays.asList(files)));
    }
    
    public DeleteProgram(ArrayList<File> files) {
        initialize(files);
    }
    
    private void initialize(ArrayList<File> files) {
        mFiles = files;
    }
    
    @Override
    public CommandEvent execute(final CommandTask task) {
        final ExecutionCallback callback = new ExecutionCallback() {

            @Override
            public void onProgress(CommandEvent event) {
                requestProgress();
            }
            
        };
        final ExecutionCanceller canceller = new ExecutionCanceller() {

            @Override
            public boolean isCancelled() {
                return task != null ? task.isCancelled() : false;
            }
            
        };
        final ArrayList<File> files = mFiles;
        for (File file : files) {
            CommandHelper.delete(file, callback, canceller);
        }
        return null;
    }

    @Override
    public void requestProgress() {
        // TODO Auto-generated method stub
        
    }

}
