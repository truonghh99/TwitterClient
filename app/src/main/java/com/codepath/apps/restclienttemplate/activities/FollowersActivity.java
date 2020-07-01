package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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
                Log.i(TAG, "onSuccess when populate follower list! " + json.toString());
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    users = User.fromJsonArray(jsonArray);
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
}