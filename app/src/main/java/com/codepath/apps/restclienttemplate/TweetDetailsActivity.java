package com.codepath.apps.restclienttemplate;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    ImageView ivProfile;
    TextView tvName;
    TextView tvUsername;
    TextView tvBody;
    ImageView ivMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        // getting IDs from activity_tweet_detail
        ivProfile = (ImageView) findViewById(R.id.ivProfileDetail);
        tvName = (TextView) findViewById(R.id.tvNameDetail);
        tvUsername = (TextView) findViewById(R.id.tvUsernameDetail);
        tvBody = (TextView) findViewById(R.id.tvBodyDetail);
        ivMedia = (ImageView) findViewById(R.id.ivMediaDetail);

        //Get tweet that has been selected
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // set each component of the detail
        tvName.setText(tweet.user.name);
        tvUsername.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        Glide.with(this).load(tweet.user.profileImageURL).
                apply(new RequestOptions().circleCrop()).into(ivProfile);
        if (tweet.imageURL != null) {
            ivMedia.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.imageURL).apply(new RequestOptions().
                    centerCrop().transform(new RoundedCorners(100))).into(ivMedia);
        }else{
            ivMedia.setVisibility(View.GONE);
        }


    }
}