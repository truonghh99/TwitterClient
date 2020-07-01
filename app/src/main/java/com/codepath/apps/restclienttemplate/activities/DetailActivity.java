package com.codepath.apps.restclienttemplate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.sql.Time;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvUsername;
    TextView tvName;
    TextView tvTimeStamp;
    ImageView ivMedia;
    TextView tvRetweetCount;
    TextView tvLikeCount;
    ImageView ivReply;
    ImageView ivRetweet;
    ImageView ivLike;
    Tweet tweet;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDetailBinding activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        setUpToolbar(activityDetailBinding);

        ivProfileImage = activityDetailBinding.ivProfileImage;
        tvBody = activityDetailBinding.tvBody;
        tvUsername = activityDetailBinding.tvUserName;
        tvName = activityDetailBinding.tvName;
        tvTimeStamp = activityDetailBinding.tvTimestamp;
        ivMedia = activityDetailBinding.ivMedia;
        tvRetweetCount = activityDetailBinding.tvRetweetCounter;
        tvLikeCount = activityDetailBinding.tvLikeCounter;
        ivReply = activityDetailBinding.ivReply;
        ivRetweet = activityDetailBinding.ivRetweet;
        ivLike = activityDetailBinding.ivLike;
        client = TwitterApp.getRestClient(this);


        final Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(TimelineActivity.KEY_TWEET));
        Log.e(TAG, tweet.toString());

        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvUsername.setText("@" + tweet.user.userName);
        tvTimeStamp.setText(tweet.createdAt);
        tvRetweetCount.setText(convertNumericValToString(tweet.numRetweet, "RETWEETS"));
        tvLikeCount.setText(convertNumericValToString(tweet.numLike, "FAVORITES"));

        Glide.with(DetailActivity.this).load(tweet.user.profileImgUrl).into(ivProfileImage);

        if (tweet.imgUrl != null) {
            Glide.with(DetailActivity.this).load(tweet.imgUrl).into(ivMedia);
        } else {
            // Avoid reusing image from last item in recycler view
            ivMedia.setImageResource(0);
        }

        // Notify when reply icon is clicked
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ComposeActivity.class);
                intent.putExtra(TimelineActivity.KEY_USER_NAME, tweet.user.userName);
                startActivity(intent);
            }
        });

        // Notify when retweet icon is clicked
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long num = readNumericValFromTv(tvRetweetCount, "RETWEETS");
                if (ivRetweet.getColorFilter() == null) {
                    ivRetweet.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inline_action_retweet), android.graphics.PorterDuff.Mode.MULTIPLY);
                    tvRetweetCount.setText(convertNumericValToString(num + 1, "RETWEETS"));
                } else {
                    ivRetweet.setColorFilter(null);
                    tvRetweetCount.setText(convertNumericValToString(num - 1, "RETWEETS"));
                }
                client.likeTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(getApplicationContext(), "Retweed!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onSuccess to retweet");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        client.unlikeTweet(new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Toast.makeText(getApplicationContext(), "Unretweed!", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onSuccess to unretweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "onFailure to unretweet");
                            }
                        }, tweet.id);
                    }
                }, tweet.id);
            }
        });

        // Notify when like icon is clicked
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long num = readNumericValFromTv(tvLikeCount, "FAVORITES");
                if (ivLike.getColorFilter() == null) {
                    ivLike.setColorFilter(ContextCompat.getColor(DetailActivity.this, R.color.inline_action_like), android.graphics.PorterDuff.Mode.MULTIPLY);
                    tvLikeCount.setText(convertNumericValToString(num + 1, "FAVORITES"));
                } else {
                    ivLike.setColorFilter(null);
                    tvLikeCount.setText(convertNumericValToString(num - 1, "FAVORITES"));
                }
                client.likeTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(getApplicationContext(), "Liked!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onSuccess to like");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        client.unlikeTweet(new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Toast.makeText(getApplicationContext(), "Unliked!", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onSuccess to unlike");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.i(TAG, "onFailure to unlike");
                            }
                        }, tweet.id);
                    }
                }, tweet.id);
            }
        });
    }

    private long readNumericValFromTv(TextView tv, String noun) {
        String text = tv.getText().toString();
        Log.e(TAG, text);
        int lastNumberPos = text.length() - noun.length() - 1;
        long num = Integer.parseInt(text.substring(0, lastNumberPos));
        return num;
    }

    private String convertNumericValToString(long num, String noun) {
        return "" + num + " " + noun;
    }

    private void setUpToolbar(com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding activityDetailBinding) {
        Toolbar toolbar = activityDetailBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}