package com.codepath.apps.restclienttemplate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.fragment.ComposeFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.parceler.Parcels;

import java.sql.Time;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
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

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(TimelineActivity.KEY_TWEET));
        Log.e(TAG, tweet.toString());

        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvUsername.setText("@" + tweet.user.userName);
        tvTimeStamp.setText(tweet.createdAt);
        updateRetweetStatus();
        updateLikeStatus();

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 0; // crop margin, set to 0 for corners with no crop

        Glide.with(DetailActivity.this)
                .load(tweet.user.profileImgUrl)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(ivProfileImage);

        if (tweet.imgUrl != null) {
            Glide.with(DetailActivity.this).load(tweet.imgUrl).into(ivMedia);
        } else {
            // Avoid reusing image from last item in recycler view
            ivMedia.setImageResource(0);
        }

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog(tweet.user.userName);
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet.attemptToRetweet(client, DetailActivity.this);
                updateRetweetStatus();
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet.attemptToLike(client, DetailActivity.this);
                updateLikeStatus();
            }
        });
    }

    private void updateRetweetStatus() {
        tvRetweetCount.setText(tweet.numRetweet + " RETWEETS");
        if (tweet.retweeded == true) {
            ivRetweet.setColorFilter(ContextCompat.getColor(this, R.color.inline_action_retweet), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            ivRetweet.setColorFilter(null);
        }
    }

    private void updateLikeStatus() {
        tvLikeCount.setText(tweet.numLike + " FAVORITES");
        if (tweet.liked == true) {
            ivLike.setColorFilter(ContextCompat.getColor(this, R.color.inline_action_like), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            ivLike.setColorFilter(null);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem miHome = menu.findItem(R.id.miHome);
        Drawable newIcon = (Drawable) miHome.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        miHome.setIcon(newIcon);

        miHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.putExtra("tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        });

        return true;
    }


    private void setUpToolbar(com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding activityDetailBinding) {
        Toolbar toolbar = activityDetailBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void showComposeDialog(String targetUser) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment editNameDialogFragment = ComposeFragment.newInstance(targetUser);
        editNameDialogFragment.show(fm, "fragment_compose");
    }
}