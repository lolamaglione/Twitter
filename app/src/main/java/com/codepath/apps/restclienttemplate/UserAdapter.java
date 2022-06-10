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
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

/**
 * this is the user adapter. This class will handle binding the user recycler view
 * in activities like follower and following acitvity.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    // define the context/activity
    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }


    // for each row (user), inflate the layout for each user
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        // get the data at the position
        User user = users.get(position);

        // bind the tweet at the view holder with the view holder
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        // set up features of the user
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvUsername;
        TextView tvBio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivFollowingProfile);
            tvName = itemView.findViewById(R.id.tvNameFollowing);
            tvUsername = itemView.findViewById(R.id.tvUsernameFollowing);
            tvBio = itemView.findViewById(R.id.tvBioFollowing);
        }

        public void bind(User user) {

            // set the values for each aspect of the user
            tvUsername.setText(user.screenName);
            tvName.setText(user.name);
            tvBio.setText(user.profileBio);
            Glide.with(context).load(user.profileImageURL).apply(new RequestOptions().circleCrop()).into(ivProfileImage);

        }
    }
}
