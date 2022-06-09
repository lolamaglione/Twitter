package com.codepath.apps.restclienttemplate;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    Tweet tweet;
    private FloatingActionButton floatingButtonCompose;
    private ActivityComposeBinding binding;

    public static final int MAX_TWEET_LENGTH = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_compose);
        //activity_compose.xml --> ActivityComposeBinding
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);
        //tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        client = TwitterApp.getRestClient(this);
//        etCompose = findViewById(R.id.etCompose);
//        btnTweet = findViewById(R.id.btnTweet);
        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;
        //etCompose.setText("@" + );
        // Set click listener on button
        String username = getIntent().getStringExtra("username");
        if (username != null){
            etCompose.setText("@" + username + " ");
        }
        btnTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();

                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                           Tweet tweet = Tweet.fromJson(json.jsonObject);
                           Log.i(TAG, "Published tweet says: " + tweet);
                           Intent intent = new Intent();
                           intent.putExtra("tweet", Parcels.wrap(tweet));
                           // set result code and bundle data for response
                           setResult(RESULT_OK, intent);
                            // closes the activity, pass data to parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
            }
        });

    }
}