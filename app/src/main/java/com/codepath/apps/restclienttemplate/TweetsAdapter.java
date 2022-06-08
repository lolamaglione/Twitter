package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter  extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    Context context;
    List<Tweet> tweets;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // for each row, inflate the layout for a tweet
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // returns a view
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        // wraps it in the view holder that we just created
        return new ViewHolder(view);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data at position
        Tweet tweet = tweets.get(position);
        // bind the tweet at the view holder with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvName;
        TextView tvTimeAgo;
        // itemView = representation of one row of the recyclerView
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName= itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvUsername);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvTimeAgo = itemView.findViewById(R.id.tvTime);
        }

        // take out the different attributes of the screen and use it to fill out what we have on screen
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@"+tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvTimeAgo.setText(tweet.relativeTimeAgo);
            Glide.with(context).load(tweet.user.profileImageURL).into(ivProfileImage);
            if (tweet.imageURL != null) {
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.imageURL).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(25))).into(ivMedia);
            }else{
                ivMedia.setVisibility(View.GONE);
            }
        }
    }

}
