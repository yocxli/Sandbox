package net.yocxli.playlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistOpenHelper extends SQLiteOpenHelper {
    private static final String DB = "playlist";
    private static final int VERSION = 1;
    
    static final String TABLE_PLAYLISTS = "playlists";
    static final String TABLE_ITEMS = "items";
    

    public PlaylistOpenHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLAYLISTS + "("
                + "_id integer primary key autoincrement,"
                + "name text not null,"
                + "date_modified integer not null)");
        
        db.execSQL("create table " + TABLE_ITEMS + "("
                + "playlist_id integer not null,"
                + "audio_id integer not null,"
                + "order integer not null,"
                + "primary key (playlist_id, audio_id))");
        
        db.execSQL("create unique index idx_list on " + TABLE_ITEMS + " (playlist_id, order)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }

}
