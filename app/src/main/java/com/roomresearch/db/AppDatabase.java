package com.roomresearch.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.roomresearch.Constants;
import com.roomresearch.db.dao.CommentDao;
import com.roomresearch.db.dao.NewsDao;
import com.roomresearch.db.dao.UserDao;
import com.roomresearch.db.entity.Comment;
import com.roomresearch.db.entity.News;
import com.roomresearch.db.entity.User;

import java.net.URL;

/**
 * Created by dima on 6/29/17.
 */
@Database(entities = {User.class, News.class, Comment.class}, version = Constants.DB_VERSION)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewsDao newsDao();
    public abstract CommentDao commentDao();


    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE News "
                    + " ADD COLUMN someAwesomeFiled TEXT");
        }
    };
}
