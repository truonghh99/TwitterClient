package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id, Tweet.imgUrl AS tweet_imgUrl, User.*, " +
            "Tweet.numLike AS tweet_numLike, Tweet.numRetweet AS tweet_numRetweet, Tweet.liked AS tweet_liked, Tweet.retweeded as tweet_retweeded " +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY createdAt ASC LIMIT 20")

    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
