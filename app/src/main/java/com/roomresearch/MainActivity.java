package com.roomresearch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.roomresearch.db.dao.UserDao;
import com.roomresearch.db.entity.News;
import com.roomresearch.db.entity.User;
import com.roomresearch.db.entity.query.NewsWithOwnerLogin;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Observer<List<News>> {

    private String TAG = MainActivity.class.getName();

    private TextView countOfNewsFromFlowable, countOfNewsFromLiveData;
    private Button addNews, addUser;

    private int userId = 0;
    private LiveData<List<News>> livaData;
    private Flowable flowableSubscriber;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countOfNewsFromFlowable = (TextView)findViewById(R.id.news_count_from_flowable);
        countOfNewsFromLiveData = (TextView)findViewById(R.id.news_count_from_live_data);
        addNews = (Button)findViewById(R.id.add_news);
        addUser = (Button)findViewById(R.id.create_user);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        User user = createUser();
                        UserDao userDao = App.getAppDatabase().userDao();
                        userDao.insertAll(user);
                        User savedUser = userDao.loadByLogin(user.getLogin());
                        userId = savedUser.getId();
                    }
                }.start();

            }
        });

        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        News news = initStubNews(userId);
                        App.getAppDatabase().newsDao().insertAll(news);
                    }
                }.start();
            }
        });


        livaData = App.getAppDatabase().newsDao().loadNewsLiveData();
        livaData.observeForever(MainActivity.this);

        flowableSubscriber = App.getAppDatabase().newsDao().loadNewsFlowable();
        disposable.add(flowableSubscriber
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<News>>() {
            @Override
            public void accept(List<News> newses) throws Exception {
                Log.d(TAG, "onNext " + Thread.currentThread().getName());
                countOfNewsFromFlowable.setText("News count from flowable: " + newses.size());
            }
        }));

    }

    private User createUser() {
        String login = "login" + Calendar.getInstance().getTimeInMillis();
        User user = new User();
        user.setImageURL("testurl");
        user.setLogin(login);
        user.setPassword("pass");
        return user;
    }

    private News initStubNews(int userId) {
        News news = new News();
        news.setCreatedDate(Calendar.getInstance().getTime());
        news.setNews("News 1");
        news.setOwnerId(userId);
        news.setPreview("Some preview");
        news.setTitle("Awesome News");
        return news;
    }

    @Override
    public void onChanged(@Nullable List<News> newses) {
        countOfNewsFromLiveData.setText("News count from live data: " + newses.size());
    }

    @Override
    protected void onStop() {
        livaData.removeObserver(this);
        disposable.clear();
        super.onStop();
    }
}
