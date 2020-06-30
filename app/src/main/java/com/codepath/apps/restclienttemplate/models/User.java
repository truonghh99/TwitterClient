package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public String userName;
    public String imgUrl;

    // Empty constructor needed for parcel library
    public User() {}

    // Extract user's information from the given JSON object
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.userName = jsonObject.getString("screen_name");
        user.name = jsonObject.getString("name");
        user.imgUrl = jsonObject.getString("profile_image_url_https");
        return user;
    }
}
