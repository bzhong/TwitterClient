package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

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

        client = TwitterApplication.getRestClient();
//        client.getUserInfo(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                user = User.fromJson(response);
//                populateComposeHeader(user);
//            }
//        });
    }

    private void populateComposeHeader(User user) {
        tweetText = (EditText) findViewById(R.id.tweetText);
        textCount = (TextView) findViewById(R.id.textCount);
        tweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("DEBUG", String.valueOf(start));
                textCount.setText(String.valueOf(R.string.tweet_max_length - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView tvName = (TextView) findViewById(R.id.tvFullName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    public void onComposeTweet(View view) {
        String tweet = tweetText.getText().toString();
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                user = User.fromJson(response);
//                populateComposeHeader(user);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

}
