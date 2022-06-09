package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

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
}
