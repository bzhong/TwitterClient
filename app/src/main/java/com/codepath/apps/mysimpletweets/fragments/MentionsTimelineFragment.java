package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.populateTimeline(super.GET_STATUS_MENTIONS_TIMELINE);
    }
}
