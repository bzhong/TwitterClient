package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeFragment extends Fragment {

    private TwitterClient client;
    private User user;
    private EditText tweetText;
    private TextView textCount;
    private TextView tvName;
    private TextView tvScreenName;
    private ImageView ivProfileImage;
    private Button btnCompose;
    private OnPostSuccess listener;

    private final int MAX_TWEET_LENGTH = 140;

    public interface OnPostSuccess {
        public void onPostTweetSucceed(Tweet tweet);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostSuccess) {
            listener = (OnPostSuccess) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ComposeFragment.OnPostSuccess");
        }
    }

    public static ComposeFragment newInstance(String screenName) {
        ComposeFragment composeFragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        composeFragment.setArguments(args);
        return composeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        tweetText = (EditText) view.findViewById(R.id.tweetText);
        textCount = (TextView) view.findViewById(R.id.textCount);
        tvName = (TextView) view.findViewById(R.id.tvFullName);
        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        btnCompose = (Button) view.findViewById(R.id.btnCompose);

        return view;
    }

    @Override
    public void onStart() {
        client.getUserInfo(getArguments().getString("screen_name"), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
//                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateComposeHeader(user);
            }
        });
        super.onStart();
    }

    private void populateComposeHeader(User user) {
        tvName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
        tweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCount.setText(String.valueOf(MAX_TWEET_LENGTH - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        btnCompose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tweet = tweetText.getText().toString();
                client.postTweet(tweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJSON(response);
                        listener.onPostTweetSucceed(tweet);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }
        });
    }
}
