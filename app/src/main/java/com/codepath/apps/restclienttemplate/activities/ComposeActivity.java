package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding activityComposeBinding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(activityComposeBinding.getRoot());

        etCompose = activityComposeBinding.etCompose;
        btnTweet = activityComposeBinding.btnTweet;

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your tweet cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > 140) {
                    Toast.makeText(getApplicationContext(), "Your tweet is too long!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Yayyyy", Toast.LENGTH_SHORT).show();
            }
        });
    }
}