package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ActivityExploreBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityFollowersBinding;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityExploreBinding activityExploreBinding = ActivityExploreBinding.inflate(getLayoutInflater());
        setContentView(activityExploreBinding.getRoot());

        setUpToolbar(activityExploreBinding);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem miHome = menu.findItem(R.id.miHome);
        Drawable newIcon = (Drawable) miHome.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        miHome.setIcon(newIcon);

        miHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ExploreActivity.this, TimelineActivity.class);
                finish();
                return true;
            }
        });
        return true;
    }

    private void setUpToolbar(ActivityExploreBinding activityExploreBinding) {
        Toolbar toolbar = activityExploreBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}