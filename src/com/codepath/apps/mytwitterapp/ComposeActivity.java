package com.codepath.apps.mytwitterapp;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
	
		
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onCancel(View v) {
		this.finish();
	}
	
	public void onTweet(View v) {
		EditText etTweet = (EditText) findViewById(R.id.etTweet);
		String tweetBody = etTweet.getText().toString();
		Toast.makeText(getApplicationContext(), "clicked: " + tweetBody, Toast.LENGTH_SHORT).show();// OK
		MyTwitterApp.getRestClient().postTweet(tweetBody, new JsonHttpResponseHandler() {
			@Override 
			public void onSuccess(JSONArray json) {
				  Toast.makeText(getApplicationContext(), "tweet response received successfully", Toast.LENGTH_SHORT).show();
				  Tweet tweet = Tweet.fromJson(json).get(0);
				  processTweet(tweet);
			  }
			@Override
			public void onFailure(Throwable throwable, String error) {
				Toast.makeText(getApplicationContext(), "Request failed: " + error, Toast.LENGTH_SHORT).show();
				goBack();
				
			}
			public void handleFailureMessage(Throwable e, String responseBody) {
				Toast.makeText(getApplicationContext(), "Request failed: " + responseBody, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				goBack();
			}
		});		  
	}
	
	public void processTweet(Tweet tweet) {
		Intent data = new Intent();
		data.putExtra("tweet", tweet);
		setResult(RESULT_OK, data);
		finish();
	}
	public void goBack() {
		Intent data = new Intent();
		
		setResult(RESULT_OK, data);
		finish();
	}
}
