package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the parcelable object Tweet. This holds all the information for
 * every tweet that is created, be it of the user or of another user.
 * The tweet holds the body, the time when the tweet was created, the username of who
 * tweeted and the screen name of who tweeted. You access this class if you want to
 * access anything that is part of the tweet.
 */
@Parcel
public class Tweet {

    //body of the tweet
    public String body;
    // when tweet was created
    public String createdAt;
    public User user;
    public String imageURL;
    public String relativeTimeAgo;
    public int favoriteCount;
    public int retweet_count;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public Long tweetID;
    public boolean favorited;
    public boolean retweeted;

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
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.tweetID = jsonObject.getLong("id");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
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

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i("timeAgoCheck", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }


}
