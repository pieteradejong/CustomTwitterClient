package com.codepath.apps.mytwitterapp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

public class Tweet implements Serializable {

	private static final long serialVersionUID = -7241275184835902531L;
	private String body;
	private String created_at;
	private String relativeTime;
	private long uid;
	private boolean favorited;
	private boolean retweeted;
    private User user;
    
    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return uid;
    }
    
    public String getTimestamp() {
        return created_at;
    }
    
//    public String getRelativeTimestamp() throws ParseException {
//    	
//    	Calendar cal = new GregorianCalendar();
//    	String LARGE_TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//    	Date date;
//    	Date date = new SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH).parse(this.created_at);
//    	long ms = date.getTime();
//		try {
//			date = new SimpleDateFormat(LARGE_TWITTER_DATE_FORMAT, Locale.ENGLISH).parse(created_at);
//			cal.setTime(date);
//			ms = cal.getTimeInMillis();
//		} catch (java.text.ParseException e) {
//			ms = 0;
//			e.printStackTrace();
//		}
//    	return (String) DateUtils.getRelativeDateTimeString(null, ms, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NO_NOON, 0);
//    }
    	
    	public String getRelativeTimeAgo(String rawJsonDate) {
    		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
    		sf.setLenient(true);
    	 
    		String relativeDate = "";
    		try {
    			long dateMillis = sf.parse(rawJsonDate).getTime();
    			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
    					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	 
    		return relativeDate;
    	}

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
    
    public Tweet(String body) {
    	this.body = body;
    }
    public Tweet() {
    }
    
    
    
 // Decodes business json into business model object
    public static Tweet fromJson(JSONObject jsonObject) {
      Tweet tweet = new Tweet();
          // Deserialize json into object fields
      try {
    	  tweet.body = jsonObject.getString("text");
      	  tweet.uid = jsonObject.getLong("id");
      	  tweet.created_at = jsonObject.getString("created_at");
//          try {
//			tweet.relativeTime = tweet.getRelativeTimestamp();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
      	  tweet.relativeTime = tweet.getRelativeTimeAgo(tweet.created_at);
      	  tweet.favorited = jsonObject.getBoolean("favorited");
      	  tweet.retweeted = jsonObject.getBoolean("retweeted");
          tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
          } catch (JSONException e) {
              e.printStackTrace();
              return null;
          }
      // Return new object
      return tweet;
    }
    
 // Decodes array of business json results into business model objects
    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
            	tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
              tweets.add(tweet);
            }
        }
        return tweets;
    }
}
