package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * this is the user class. In the user class, similar than the tweet, you can access
 * information about the user. Here you get the profile image, the username, amount of
 * followers and following.
 */
@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageURL;
    public String profileBio;
    public int followers;
    public int following;
    public long user_id;

    public User(){

    }
    // create a User item
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageURL = jsonObject.getString("profile_image_url_https");
        user.profileBio = jsonObject.getString("description");
        user.followers = jsonObject.getInt("followers_count");
        user.following = jsonObject.getInt("friends_count");
        user.user_id = jsonObject.getLong("id");

        return user;
    }

    public static List<User> fromJsonArrayUser(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        // go through the JSON array and add to the tweets list
        for(int i = 0; i < jsonArray.length(); i++){
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return users;
    }
}
