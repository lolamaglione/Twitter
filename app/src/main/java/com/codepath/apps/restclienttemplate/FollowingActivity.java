package com.codepath.apps.restclienttemplate;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.databinding.ActivityFollowingBinding;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * This activity will lead the page where we will see who the user that is clicked on
 * is following.
 */

public class FollowingActivity extends AppCompatActivity {

    TwitterClient client;
    RecyclerView rvUsers;
    List<User> users;
    UserAdapter adapter;
    ActivityFollowingBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        // initialize the recycler view
        rvUsers = binding.rvUsers;

        users = new ArrayList<>();

        adapter = new UserAdapter(this, users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(linearLayoutManager);
        rvUsers.setAdapter(adapter);

        populateUserTimeline();
    }

    private void populateUserTimeline() {
        client.getFollowingTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("following activity", "onSuccess!" + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    System.out.println("array" + jsonArray);
                    users.addAll(User.fromJsonArrayUser(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    //
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        }, Parcels.unwrap(getIntent().getParcelableExtra("user_id")));
    }
}