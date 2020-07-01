package com.codepath.apps.restclienttemplate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeFragment extends DialogFragment {

    private static final String TAG = "ComposeFragment";
    private EditText etCompose;
    private Button btTweet;
    private String tweetContent;
    private TwitterClient client;
    private String targetUser;
    private ImageView ivClose;

    public ComposeFragment() {
    }

    public static ComposeFragment newInstance(String targetUser) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("Reply", targetUser);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        btTweet = (Button) view.findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(getActivity());
        ivClose = (ImageView) view.findViewById(R.id.ivClose);

        // Fetch arguments from bundle and set title
        targetUser = getArguments().getString("Reply");
        if (targetUser != "") {
            etCompose.setText("@" + targetUser + " ");
            btTweet.setText("Reply");
        }
        etCompose.requestFocus();
        btTweet.setOnClickListener(publish);
        ivClose.setOnClickListener(close);
    }

    private final View.OnClickListener close = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            close();
        }
    };

    private void close() {
        getActivity().onBackPressed();
    }
    private final View.OnClickListener publish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tweetContent = etCompose.getText().toString();
            if (tweetContent.isEmpty()) {
                Toast.makeText(getActivity(), "Your tweet is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tweetContent.length() > 280) {
                Toast.makeText(getActivity(), "Your tweet is too long!", Toast.LENGTH_SHORT).show();
                return;
            }
            client.publishTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to publish tweet");
                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
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
            if (targetUser.isEmpty()) {
                Toast.makeText(getActivity(), "Tweeted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Replied!", Toast.LENGTH_SHORT).show();
            }
            close();
        }
    };
}