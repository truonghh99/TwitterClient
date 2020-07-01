package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.DetailActivity;
import com.codepath.apps.restclienttemplate.databinding.FragmentUserBinding;
import com.codepath.apps.restclienttemplate.models.User;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends DialogFragment {


    private static final String TAG = "UserFragment";

    private User user;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvDescription;
    private TextView tvFollowingCount;
    private TextView tvFollowerCount;

    public UserFragment() {
    }

    public static UserFragment newInstance(User user) {
        UserFragment fragment = new UserFragment();
        fragment.user = user;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, user.userName);

        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvUsername = (TextView) view.findViewById(R.id.tvUserName);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvFollowerCount = (TextView) view.findViewById(R.id.tvNumFollowers);
        tvFollowingCount = (TextView) view.findViewById(R.id.tvNumFollowing);

        tvName.setText(user.name);
        tvUsername.setText("@" + user.userName);
        tvDescription.setText(user.description);
        tvFollowingCount.setText(user.followingCount);
        tvFollowerCount.setText(user.followersCount);

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 0; // crop margin, set to 0 for corners with no crop

        Glide.with(getActivity())
                .load(user.profileImgUrl)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(ivProfileImage);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
}