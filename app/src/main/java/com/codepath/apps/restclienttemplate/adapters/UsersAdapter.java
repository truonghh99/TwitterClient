package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.DetailActivity;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.databinding.ItemUserBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final String TAG = "UsersAdapter";
    private Context context;
    private List<User> users;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClickListener(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemUserBinding);
    }

    public UsersAdapter(Context context, List<User> users, OnClickListener onClickListener) {
        this.context = context;
        this.users = users;
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvUsername;
        Button btFollow;

        public ViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            ivProfileImage = itemUserBinding.ivProfileImage;
            tvName = itemUserBinding.tvName;
            tvUsername = itemUserBinding.tvUserName;
            btFollow = itemUserBinding.btnFollow;
        }

        public void bind(final User user) {
            tvName.setText(user.name);
            tvUsername.setText("@" + user.userName);

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            Glide.with(context)
                    .load(user.profileImgUrl)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivProfileImage);

            if (user.friendStatus == true) {
                btFollow.setBackgroundColor(context.getResources().getColor(R.color.twitter_blue_fill_pressed));
                btFollow.setText("Following");
                btFollow.setTextColor(Color.WHITE);
            } else {
                btFollow.setBackgroundColor(Color.WHITE);
                btFollow.setText("Follow");
                btFollow.setTextColor(context.getResources().getColor(R.color.twitter_blue_fill_pressed));
            }

            btFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }
}
