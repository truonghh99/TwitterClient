package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public final String TAG = "ComposeActivity";

    private EditText etCompose;
    private Button btnTweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding activityComposeBinding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(activityComposeBinding.getRoot());

        client = TwitterApp.getRestClient(this);
        etCompose = activityComposeBinding.etCompose;
        btnTweet = activityComposeBinding.btnTweet;

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your tweet cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > 140) {
                    Toast.makeText(getApplicationContext(), "Your tweet is too long!", Toast.LENGTH_SHORT).show();
                    return;
                }
                client.publishTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweetContent);
                        } catch (JSONException e) {
                            Log.e(TAG, "Cannot extract tweet");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                }, tweetContent);
                Toast.makeText(getApplicationContext(), "Tweeted!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}