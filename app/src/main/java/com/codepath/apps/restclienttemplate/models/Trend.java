package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trend {

    public String name;
    public String url;
    public int index;

    public static List<Trend> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Trend> trends = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Trend trend = new Trend();
            trend.name = jsonObject.getString("name");
            trend.url = jsonObject.getString("url");
            trend.index = i;
        }
        return trends;
    }
}
