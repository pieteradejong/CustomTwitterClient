package com.codepath.apps.mytwitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mytwitterapp.EndlessScrollListener;
import com.codepath.apps.mytwitterapp.MyTwitterApp;
import com.codepath.apps.mytwitterapp.R;
import com.codepath.apps.mytwitterapp.Tweet;
import com.codepath.apps.mytwitterapp.TweetsAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		
		View v =  inf.inflate(R.layout.fragment_tweets_list, parent, false);
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);//YES 
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        	loadTweets(page);
	        }
	    });
		
		return v;
	}
	
	public void loadTweets(int page) {
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				TweetsAdapter adapter = (TweetsAdapter) lvTweets.getAdapter();
				adapter.addAll(Tweet.fromJson(jsonTweets));
			}
		}, page);
	}
	
}
