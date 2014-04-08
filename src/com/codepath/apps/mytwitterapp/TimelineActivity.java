package com.codepath.apps.mytwitterapp;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitterapp.fragments.MentionsFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	ListView lvTweets;
	
	private final int REQUEST_CODE = 20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
		
//		lvTweets = (ListView) findViewById(R.id.lvTweets);
//		lvTweets.setOnScrollListener(new EndlessScrollListener() {
//	        @Override
//	        public void onLoadMore(int page, int totalItemsCount) {
//	        	loadTweets(page);
//	        }
//	    });
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
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
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
		  Toast.makeText(this, "YES activity result success", Toast.LENGTH_SHORT).show();
		  Tweet tweet = (Tweet) data.getSerializableExtra("tweet");

//	     lvTweets = (ListView) findViewById(R.id.lvTweets);
	     TweetsAdapter adapter = (TweetsAdapter) lvTweets.getAdapter();
	     adapter.insert(tweet, 0);
	     adapter.notifyDataSetChanged();
	    
	  } else {
		  Toast.makeText(this, "activity result failed", Toast.LENGTH_SHORT).show();
	  }
	}
	private void setupNavigationTabs() {
	    ActionBar actionbar = getActionBar();
	    actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionbar.setDisplayShowTitleEnabled(true);
	    Tab tabHome = actionbar.newTab().setText("Home")
	    		.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
	    		.setTabListener(this);
	    Tab tabMentions = actionbar.newTab().setText("Mentions")
	    		.setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_mentions)
	            .setTabListener(this);
	    
	    actionbar.addTab(tabHome);
	    actionbar.addTab(tabMentions);
	    actionbar.selectTab(tabHome);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fts = manager.beginTransaction(); 
		if(tab.getTag() == "HomeTimelineFragment") {
        	// set to home timeline
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
        } else {
        	// set to mentions timeline
        	fts.replace(R.id.frame_container, new MentionsFragment());
        }
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		
	}	
}
