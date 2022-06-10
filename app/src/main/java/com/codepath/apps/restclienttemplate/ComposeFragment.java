package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {

    private EditText etCompose;
    Button btnTweet;
    TwitterClient client = TwitterApp.getRestClient(getActivity());
    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "component string";
    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String title) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etComposeFragment);
        btnTweet = view.findViewById(R.id.btnTweetFragment);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");

        btnTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()){
                    Toast.makeText(getActivity(), "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(getActivity(), "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
                getDialog().dismiss();
            }
        });
    }



}