package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.UsersAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityFollowersBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class FollowersActivity extends AppCompatActivity {

    private static final String TAG = "FollowersActivity";
    private RecyclerView rvUsers;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TwitterClient client;
    private RecyclerView.LayoutManager layoutManager;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFollowersBinding activityFollowersBinding = ActivityFollowersBinding.inflate(getLayoutInflater());
        setContentView(activityFollowersBinding.getRoot());

        setUpToolbar(activityFollowersBinding);

        client = TwitterApp.getRestClient(this);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(FollowersActivity.this, users);
        layoutManager = new LinearLayoutManager(this);

        userId = getIntent().getLongExtra(TimelineActivity.KEY_USER_ID, 0);

        rvUsers = activityFollowersBinding.rvUsers;
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(usersAdapter);

        populateFolowersList();
    }

    private void populateFolowersList() {
        client.getFollowers(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("users");
                    Log.i(TAG, "onSuccess when populate follower list! " + jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    users = User.fromJsonArray(jsonArray);
                    Log.i(TAG, String.valueOf(users.size()));
                    usersAdapter.addAll(users);
                    usersAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure! " + response, throwable);
            }
        }, userId);
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
                Intent intent = new Intent(FollowersActivity.this, TimelineActivity.class);
                finish();
                return true;
            }
        });

        return true;
    }


    private void setUpToolbar(com.codepath.apps.restclienttemplate.databinding.ActivityFollowersBinding activityFollowersBinding) {
        Toolbar toolbar = activityFollowersBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}