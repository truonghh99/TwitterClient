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
import com.codepath.apps.restclienttemplate.databinding.ItemTrendBinding;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.databinding.ItemUserBinding;
import com.codepath.apps.restclienttemplate.models.Trend;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder> {

    private final String TAG = "TrendsAdapter";
    private Context context;
    private List<Trend> trends;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClickListener(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTrendBinding itemTrendBinding = ItemTrendBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemTrendBinding);
    }

    public TrendsAdapter(Context context, List<Trend> trends, OnClickListener onClickListener) {
        this.context = context;
        this.trends = trends;
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trend trend = trends.get(position);
        holder.bind(trend);
    }

    @Override
    public int getItemCount() {
        return trends.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        trends.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Trend> list) {
        trends.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex;
        TextView tvName;
        Button btExplore;

        public ViewHolder(@NonNull ItemTrendBinding itemTrendBinding) {
            super(itemTrendBinding.getRoot());

            tvName = itemTrendBinding.tvTrendName;
            tvIndex = itemTrendBinding.tvIndex;
            btExplore = itemTrendBinding.btExplore;
        }

        public void bind(final Trend trend) {
            tvName.setText(trend.name);
            tvIndex.setText(trend.index + ". Trending");

            btExplore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }
}
