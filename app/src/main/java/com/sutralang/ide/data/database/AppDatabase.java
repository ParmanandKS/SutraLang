package com.sutralang.ide.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.sutralang.ide.data.dao.SnippetDao;
import com.sutralang.ide.data.dao.UserDao;
import com.sutralang.ide.data.entity.Snippet;
import com.sutralang.ide.data.entity.User;

@Database(entities = {User.class, Snippet.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract SnippetDao snippetDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "sutra_lang_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
