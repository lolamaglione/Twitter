package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    //body of the tweet
    public String body;
    // when tweet was created
    public String createdAt;
    public User user;
    public String imageURL;
    public String relativeTimeAgo;

    // empty constructor needed
    public Tweet(){

    }
    // create a tweet item with data from the JSONObject.
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")){
           tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }

        if(jsonObject.getJSONObject("entities").has("media")){
            JSONArray mediaArray = jsonObject.getJSONObject("entities").getJSONArray("media");
            if (mediaArray.length() != 0){
                tweet.imageURL = mediaArray.getJSONObject(0).getString("media_url_https");
            }
        }

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.relativeTimeAgo = tweet.getRelativeTimeAgo(tweet.createdAt);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    // get a list of tweets that is returned from the JSON file.
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        // go through the JSON array and add to the tweets list
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}
