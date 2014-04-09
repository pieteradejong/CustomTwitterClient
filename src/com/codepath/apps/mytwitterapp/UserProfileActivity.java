package com.codepath.apps.mytwitterapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mytwitterapp.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileActivity extends ProfileActivity {
	private String username;
	private ListView lvTweets;
	TweetsAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		username = getIntent().getStringExtra("username");
		loadProfileInfo();
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(username);
		ft.replace(R.id.your_placeholder, fragmentUserTimeline);
		ft.commit();
	}
	
	public void loadTweets(int page) {
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = (TweetsAdapter) lvTweets.getAdapter();
		MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
			}
		}, page, username);
	}
	
	private void loadProfileInfo() {
		MyTwitterApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User u = User.fromJson(json);
				getActionBar().setTitle("@" + u.getScreenName());
				pupolateProfileHeader(u);
			}
		}, username);
	}
	
	private void pupolateProfileHeader(User user) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + "Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}
}
