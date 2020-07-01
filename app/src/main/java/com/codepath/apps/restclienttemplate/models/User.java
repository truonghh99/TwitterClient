package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {

    public static final String TAG = "UserModel";
    @ColumnInfo
    @PrimaryKey
    public Long id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String userName;

    @ColumnInfo
    public String profileImgUrl;

    @ColumnInfo
    public String followersCount;

    @ColumnInfo
    public String followingCount;

    @ColumnInfo
    public String description;

    // Empty constructor needed for parcel library
    public User() {}

    // Extract user's information from the given JSON object
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.userName = jsonObject.getString("screen_name");
        user.name = jsonObject.getString("name");
        user.profileImgUrl = jsonObject.getString("profile_image_url_https");
        user.id = jsonObject.getLong("id");
        user.followersCount = jsonObject.getString("followers_count");
        user.followingCount = jsonObject.getString("friends_count");
        user.description = jsonObject.getString("description");
        return user;
    }

    // Extract user's information from the given JSON Array
    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        Log.e(TAG, "start extracting");
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            User user = fromJson(jsonArray.getJSONObject(i));
            users.add(user);
        }
        return users;
    }

    public static List<User> fromJsonTweetArray(List<Tweet> tweetsFromNetwork) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tweetsFromNetwork.size(); ++i) {
            users.add(tweetsFromNetwork.get(i).user);
        }
        return users;
    }
}
