package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTweetBinding itemTweetBinding = ItemTweetBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemTweetBinding);
    }

    public TweetsAdapter(Context context, List<Tweet> tweets, OnClickListener replyOnClickListener, OnClickListener retweetOnClickListener,
                         OnClickListener likeOnClickListener) {
        this.context = context;
        this.tweets = tweets;
        this.replyOnClickListener = replyOnClickListener;
        this.retweetOnClickListener = retweetOnClickListener;
        this.likeOnClickListener = likeOnClickListener;
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
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvUsername.setText("@" + tweet.user.userName);
            tvTimeStamp.setText(tweet.createdAt);
            tvRetweetCount.setText(tweet.numRetweet.toString());
            tvLikeCount.setText(tweet.numLike.toString());

            Glide.with(context).load(tweet.user.profileImgUrl).into(ivProfileImage);
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
                    if (ivRetweet.getColorFilter() == null) {
                        ivRetweet.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_retweet), android.graphics.PorterDuff.Mode.MULTIPLY);
                        increaseNumericTextView(tvRetweetCount);
                    } else {
                        ivRetweet.setColorFilter(null);
                        decreaseNumericTextView(tvRetweetCount);
                    }
                    retweetOnClickListener.onClickListener(getAdapterPosition());
                }
            });

            // Notify when like icon is clicked
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ivLike.getColorFilter() == null) {
                        ivLike.setColorFilter(ContextCompat.getColor(context, R.color.inline_action_like), android.graphics.PorterDuff.Mode.MULTIPLY);
                        increaseNumericTextView(tvLikeCount);
                    } else {
                        ivLike.setColorFilter(null);
                        decreaseNumericTextView(tvLikeCount);
                    }
                    likeOnClickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }

    private void increaseNumericTextView(TextView tv) {
        tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) + 1));
    }

    private void decreaseNumericTextView(TextView tv) {
        tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) - 1));
    }
}
