package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screenName = getArguments().getString("screen_name");
        super.populateTimeline(super.GET_STATUS_USER_TIMELINE);
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }
}
