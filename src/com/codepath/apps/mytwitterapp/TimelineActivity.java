package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	ListView lvTweets;
	
	private final int REQUEST_CODE = 20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        	loadTweets(page);
	        }
	    });
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.compose) {
			Toast.makeText(this, "Compose Tweet", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
			startActivityForResult(i, REQUEST_CODE);
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");

	     lvTweets = (ListView) findViewById(R.id.lvTweets);
	     TweetsAdapter adapter = (TweetsAdapter) lvTweets.getAdapter();
	     adapter.addAll(tweet);
	     
	     adapter.notifyDataSetChanged();
	    
	  } else {
		  Toast.makeText(this, "activity result failed", Toast.LENGTH_SHORT).show();
	  }
	} 

}
