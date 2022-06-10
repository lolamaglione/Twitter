package com.codepath.apps.restclienttemplate;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityProfileBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * This activity takes you to a specific users profile. This shows the tweets of that specific user
 * through a recycler view and also shows the profile picture, bio, username and name of the user.
 */
public class ProfileActivity extends AppCompatActivity {

    ImageView profilePicture;
    TextView profileName;
    TextView profileUsername;
    TextView profileBio;
    TextView profileFollowers;
    TextView profileFollowing;
    User user;
    RecyclerView rvUserTweets;
    TwitterClient client;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    List<Tweet> tweets_user;
    Button btnFollowing;

    //private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityProfileBinding.inflate(getLayoutInflater());
        //View view = binding.getRoot();
        setContentView(R.layout.activity_profile);

        profilePicture = findViewById(R.id.ivProfileProfile);
        profileName = findViewById(R.id.tvNameProfile);
        profileUsername = findViewById(R.id.tvUsernameProfile);
        profileBio = findViewById(R.id.tvBioProfile);
        profileFollowers = findViewById(R.id.tvFollowersProfile);
        profileFollowing = findViewById(R.id.tvFollowingProfile);
        rvUserTweets = findViewById(R.id.rvUserTweets);
        btnFollowing = findViewById(R.id.btnFollowing);

        client = TwitterApp.getRestClient(this);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        System.out.println("user" + user);
        profileName.setText(user.name);
        profileUsername.setText("@" + user.screenName);
        profileBio.setText(user.profileBio);
        Glide.with(this).load(user.profileImageURL).
                apply(new RequestOptions().circleCrop()).into(profilePicture);
        profileFollowers.setText("" + user.followers);
        profileFollowing.setText("" + user.following);

        tweets_user = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets_user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUserTweets.setLayoutManager(linearLayoutManager);
        rvUserTweets.setAdapter(adapter);
        populateHomeTimeline();

        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FollowingActivity.class);
                intent.putExtra("user_id", Parcels.wrap(user.user_id));
                startActivity(intent);
            }
        });

    }

    private void populateHomeTimeline() {
        client.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // check that method is running successfully.
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets_user.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                // check that the method is running not succesfully.
            }
        }, user.user_id);
    }
}