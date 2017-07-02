package com.roomresearch.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.roomresearch.db.entity.Comment;
import com.roomresearch.db.entity.News;

import java.util.List;

/**
 * Created by dima on 6/30/17.
 */
@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> getAll();

    @Query("SELECT * FROM Comment WHERE id IN (:userIds)")
    List<Comment> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(Comment... users);

    @Delete
    void delete(Comment user);
    @Update
    void update(Comment user);
}
