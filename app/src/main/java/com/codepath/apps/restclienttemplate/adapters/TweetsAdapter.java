package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClickListener(int position);
    }

    private final String TAG = "TweetsAdapter";
    private Context context;
    private List<Tweet> tweets;
    private OnClickListener replyOnClickListener;
    private OnClickListener retweetOnClickListener;
    private OnClickListener likeOnClickListener;
    private OnClickListener tweetOnClickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTweetBinding itemTweetBinding = ItemTweetBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemTweetBinding);
    }

    public TweetsAdapter(Context context, List<Tweet> tweets, OnClickListener replyOnClickListener, OnClickListener retweetOnClickListener,
                         OnClickListener likeOnClickListener, OnClickListener tweetOnClickListener) {
        this.context = context;
        this.tweets = tweets;
        this.replyOnClickListener = replyOnClickListener;
        this.retweetOnClickListener = retweetOnClickListener;
        this.likeOnClickListener = likeOnClickListener;
        this.tweetOnClickListener = tweetOnClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvUsername;
        TextView tvName;
        TextView tvTimeStamp;
        ImageView ivMedia;
        TextView tvRetweetCount;
        TextView tvLikeCount;
        ImageView ivReply;
        ImageView ivRetweet;
        ImageView ivLike;
        RelativeLayout layoutTweet;

        public ViewHolder(@NonNull ItemTweetBinding itemTweetBinding) {
            super(itemTweetBinding.getRoot());
            ivProfileImage = itemTweetBinding.ivProfileImage;
            tvBody = itemTweetBinding.tvBody;
            tvUsername = itemTweetBinding.tvUserName;
            tvName = itemTweetBinding.tvName;
            tvTimeStamp = itemTweetBinding.tvTimestamp;
            ivMedia = itemTweetBinding.ivMedia;
            tvRetweetCount = itemTweetBinding.tvRetweetCounter;
            tvLikeCount = itemTweetBinding.tvLikeCounter;
            ivReply = itemTweetBinding.ivReply;
            ivRetweet = itemTweetBinding.ivRetweet;
            ivLike = itemTweetBinding.ivLike;
            layoutTweet = itemTweetBinding.layoutTweet;
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvUsername.setText("@" + tweet.user.userName);
            tvTimeStamp.setText(tweet.createdAt);
            tvRetweetCount.setText(tweet.numRetweet.toString());
            tvLikeCount.setText(tweet.numLike.toString());

            if (tweet.retweeded == true) {
                ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_retweet), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                ivRetweet.setColorFilter(null);
            }

            if (tweet.liked == true) {
                ivLike.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_like), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                ivLike.setColorFilter(null);
            }

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            Glide.with(context)
                    .load(tweet.user.profileImgUrl)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivProfileImage);

            if (tweet.imgUrl != null) {
                Glide.with(context).load(tweet.imgUrl).into(ivMedia);
            } else {
                // Avoid reusing image from last item in recycler view
                ivMedia.setImageResource(0);
            }

            // Notify when reply icon is clicked
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyOnClickListener.onClickListener(getAdapterPosition());
                }
            });

            // Notify when retweet icon is clicked
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retweetOnClickListener.onClickListener(getAdapterPosition());
                }
            });

            // Notify when like icon is clicked
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeOnClickListener.onClickListener(getAdapterPosition());
                }
            });


            // Notify when tweet is clicked
            layoutTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetOnClickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }
}
