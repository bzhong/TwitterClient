package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.net.TwitterClient;

import org.parceler.Parcels;

public class ComposeActivity extends AppCompatActivity implements ComposeFragment.OnPostSuccess {

    private TwitterClient client;
    private User user;
    private EditText tweetText;
    private TextView textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String screenName = getIntent().getStringExtra("screen_name");
        if (savedInstanceState == null) {
            ComposeFragment composeFragment = ComposeFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.composeContainer, composeFragment);
            ft.commit();
        }
    }

    @Override
    public void onPostTweetSucceed(Tweet tweet) {
        Intent data = new Intent();
        data.putExtra("tweet", Parcels.wrap(tweet));
        setResult(RESULT_OK, data);
        finish();
    }
}
