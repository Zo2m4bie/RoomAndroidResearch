package com.roomresearch.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.roomresearch.db.entity.News;
import com.roomresearch.db.entity.User;
import com.roomresearch.db.entity.query.NewsWithOwnerLogin;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by dima on 6/30/17.
 */

@Dao
public interface NewsDao {
    @Query("SELECT * FROM News")
    List<News> getAll();

    @Query("SELECT * FROM News WHERE id IN (:userIds)")
    List<News> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(News... users);

    @Delete
    void delete(News user);
    @Update
    void update(News user);

    @Query("SELECT News.id as id, title, news, preview, ownerId, createdDate, userLogin FROM News LEFT JOIN User ON News.id = User.id")
    List<NewsWithOwnerLogin> getAllNewsWithOwnerLogin();


    @Query("SELECT * FROM News")
    public LiveData<List<News>> loadNewsLiveData();
    @Query("SELECT * FROM News")
    public Flowable<List<News>> loadNewsFlowable();
}
