package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TrendsAdapter;
import com.codepath.apps.restclienttemplate.adapters.UsersAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityExploreBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityFollowersBinding;
import com.codepath.apps.restclienttemplate.models.Trend;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ExploreActivity extends AppCompatActivity {

    private final String TAG = "ExploreActivity";
    private List<Trend> trends;
    private RecyclerView rvTrends;
    private TrendsAdapter adapter;
    private TwitterClient client;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityExploreBinding activityExploreBinding = ActivityExploreBinding.inflate(getLayoutInflater());
        setContentView(activityExploreBinding.getRoot());

        setUpToolbar(activityExploreBinding);

        // Allow users to access Twitter links via explore button
        TrendsAdapter.OnClickListener onClickListener= new TrendsAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                final Trend trend = trends.get(position);
                String url = trend.url;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        client = TwitterApp.getRestClient(this);
        trends = new ArrayList<>();
        adapter = new TrendsAdapter(ExploreActivity.this, trends, onClickListener);
        layoutManager = new LinearLayoutManager(this);

        rvTrends = activityExploreBinding.rvTrends;
        rvTrends.setLayoutManager(layoutManager);
        rvTrends.setAdapter(adapter);

        Log.e(TAG, "Populate!");
        populateTrendList();
    }

    // Load followersList
    private void populateTrendList() {
        client.getTrend(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess when populate Trends! " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    jsonArray = (JSONArray) jsonArray.getJSONObject(0).get("trends");
                    Log.i(TAG, "Object! " + jsonArray.toString());
                    Log.i(TAG, "Again! " + jsonArray.get(0).toString());
                } catch (JSONException e) {
                    Log.i(TAG, "Cannot extract trend");
                    e.printStackTrace();
                }
                try {
                    trends = Trend.fromJsonArray(jsonArray);
                    Log.i(TAG, trends.toString());
                    adapter.clear();
                    adapter.addAll(trends);
                    adapter.notifyDataSetChanged();
                    Log.e(TAG, "Notified!");
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure when populate Trends! " + response, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem miHome = menu.findItem(R.id.miHome);
        Drawable newIcon = (Drawable) miHome.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        miHome.setIcon(newIcon);

        miHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ExploreActivity.this, TimelineActivity.class);
                finish();
                return true;
            }
        });
        return true;
    }

    private void setUpToolbar(ActivityExploreBinding activityExploreBinding) {
        Toolbar toolbar = activityExploreBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}