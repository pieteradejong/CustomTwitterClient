package com.codepath.apps.mytwitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.Tweet;
import com.codepath.apps.mytwitterapp.TweetsAdapter;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	ListView lvTweets;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        adapter = new TweetsAdapter(getActivity(), tweets);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
	}
	
	public TweetsAdapter getAdapter() {
	    return adapter;
	}
}
