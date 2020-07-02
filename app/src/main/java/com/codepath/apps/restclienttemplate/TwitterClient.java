package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";
	private static final String TAG = "TwitterClient";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	public void getHomeTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}

	public void getNextPageOfTweets(JsonHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", maxId);
		client.get(apiUrl, params, handler);
	}

	public void publishTweet(JsonHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		client.post(apiUrl, params, "", handler);
	}

	public void retweetTweet(JsonHttpResponseHandler handler, Long tweetId) {
		String apiUrl = getApiUrl("statuses/retweet/" + tweetId +".json");
		RequestParams params = new RequestParams();
		client.post(apiUrl, params, "", handler);
	}

	public void unRetweetTweet(JsonHttpResponseHandler handler, Long tweetId) {
		String apiUrl = getApiUrl("statuses/unretweet/" + tweetId +".json");
		RequestParams params = new RequestParams();
		client.post(apiUrl, params, "", handler);
	}

	public void likeTweet(JsonHttpResponseHandler handler, Long tweetId) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		client.post(apiUrl, params, "", handler);
	}

	public void unlikeTweet(JsonHttpResponseHandler handler, Long tweetId) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		client.post(apiUrl, params, "", handler);
	}

	public void getFollowers(JsonHttpResponseHandler handler, Long userId) {
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		params.put("count", 200);
		client.get(apiUrl, params, handler);
	}

	public void getFollowing(JsonHttpResponseHandler handler, Long userId) {
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		params.put("count", 200);
		client.get(apiUrl, params, handler);
	}

	public void follow(JsonHttpResponseHandler handler, Long userId) {
		String apiUrl = getApiUrl("friendships/create.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		client.post(apiUrl, params, "", handler);
	}

	public void unfollow(JsonHttpResponseHandler handler, Long userId) {
		String apiUrl = getApiUrl("friendships/destroy.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		client.post(apiUrl, params, "", handler);
	}

	public void getTrend(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("trends/place.json");
		RequestParams params = new RequestParams();
		params.put("id", 1);
		client.get(apiUrl, params, handler);
	}
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
