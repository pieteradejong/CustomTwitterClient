package com.codepath.apps.mytwitterapp;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet>{
	
	private Context context;
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		imageView.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	String username = (String) v.getTag();
//	        	UserTimelineFragment.newInstance(username);
	        	
	        	Intent i = new Intent(context, UserProfileActivity.class);
	        	i.putExtra("username", username);
	        	context.startActivity(i);
	        }
	     });
		
		// set tag on view
		view.setTag(tweet.getUser().getScreenName());
		
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + "<small><font color='#777777'> " + 
		tweet.getUser().getScreenName() + ", " + tweet.getRelativeTimeAgo(tweet.getTimestamp()) +  "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		return view;
	}
}
