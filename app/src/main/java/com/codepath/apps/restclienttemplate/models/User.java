package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class User {

    @ColumnInfo
    @PrimaryKey
    public Long id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String userName;

    @ColumnInfo
    public String profileImgUrl;

    // Empty constructor needed for parcel library
    public User() {}

    // Extract user's information from the given JSON object
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.userName = jsonObject.getString("screen_name");
        user.name = jsonObject.getString("name");
        user.profileImgUrl = jsonObject.getString("profile_image_url_https");
        user.id = jsonObject.getLong("id");
        return user;
    }

    public static List<User> fromJsonTweetArray(List<Tweet> tweetsFromNetwork) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tweetsFromNetwork.size(); ++i) {
            users.add(tweetsFromNetwork.get(i).user);
        }
        return users;
    }
}
