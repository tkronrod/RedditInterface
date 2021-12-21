package com.tkronrod.RedditInterface;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import net.dean.jraw.ApiException;
import net.dean.jraw.Endpoint;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;

/**
 * Functions as an interface with the JRAW API Wrapper for Java.
 * 
 * Responsible for creating an auth token and making requests to the reddit API
 * for posts from specific subreddits.
 * 
 * 
 * @author talkronrod
 * 
 */
@Component
public class RedditReader {
	
	private RedditClient reddit;
	
	//aware this is bad practice, but here for simplicity
	private String SECRET_KEY = "dN7VoMSCO99yf-d8fTV0Afr2RIT8Kw";
	
	private String CLIENT_ID = "R9_ACucT3jNm_nUP5CFwwg";
	
	private String REDDIT_USERNAME = "Own-Beat4207";
	
	private String REDDIT_PASSWORD = "8PN9ayUM&Bk#";
	
	/**
	 * Call directly after the constructor to complete creating the auth token 
	 * and gaining access to reddit through the API
	 */
	public void start() {
		
		//Credentials for OAuth2
		Credentials oauthCredentials = Credentials.script(this.REDDIT_USERNAME, this.REDDIT_PASSWORD, 
															this.CLIENT_ID, this.SECRET_KEY);
		//user agent for the script
		UserAgent userAgent = new UserAgent("RedditInterface", 
											"com.tkronrod.RedditInterface", "1.0.0", 
											this.REDDIT_USERNAME);
		
		//Authenticate the client and automatically re-authenticates
		reddit = OAuthHelper.automatic(new OkHttpNetworkAdapter(userAgent), oauthCredentials);
	}
	
	/**
	 * Checks that the class is able to access details from reddit. 
	 * Functions as a ping
	 * @return true if connected to reddit
	 */
	public boolean health() {
		return reddit.canAccess(Endpoint.GET_SUBREDDIT_ABOUT);
	}
	
	/**
	 * Gets the top 100 posts from the front page of reddit and returns a
	 * list of their titles
	 * @return a list of titles of the top 100 posts on the front page 
	 */
	public List<String> getFrontPage() {
		
		int numArticles = 100;
		
		//browse through the top posts of the day
		DefaultPaginator<Submission> paginator = reddit.frontPage()
	            .limit(numArticles)
	            .sorting(SubredditSort.TOP)
	            .timePeriod(TimePeriod.DAY)
	            .build();
		
		//get the first page
		Listing<Submission> firstPage = paginator.next();
		
		List<String> titles = new ArrayList<String>();
		
		for(Submission post : firstPage) {
			titles.add(post.getTitle());
		}
		
		return titles;
	}
	
	/**
	 * Gets the top posts from the requested subreddit and returns them as a list in a JSON.
	 * If the requested subreddit doesn't exist or if the request is invalid because the request is an 
	 * empty string or null value, then the function returns an empty JSON with nothing in it.
	 * 
	 * 
	 * @param subreddit the name of the subreddit (without the 'r/' in front)
	 * @return a JSON with the relevant information on the top 100 posts from the requested subreddit
	 */
	public JSONObject getSubredditTop(String subreddit) {
		try {
			reddit.subreddit(subreddit).about();
		}
		//if a subreddit doesn't exist
		catch(NetworkException e) {
			return new JSONObject();
		}
		//if the value passed is a null value
		catch(IllegalArgumentException ex) {
			return new JSONObject();
		}
		//if the value passed is an empty string
		catch(ApiException exc) {
			return new JSONObject();
		}
		
		//if code gets here then the subreddit should exist and be parseable by the code
		int numArticles = 100;
	
		//browse through the top posts of the day
		DefaultPaginator<Submission> paginator = reddit.subreddit(subreddit).posts()
	            .limit(numArticles)
	            .sorting(SubredditSort.TOP)
	            .timePeriod(TimePeriod.DAY)
	            .build();
		
		//get the first page
		Listing<Submission> firstPage = paginator.next();
		
		JSONObject json = new JSONObject();
		
		List<JSONObject> posts = new ArrayList<JSONObject>();
		
		for(Submission post : firstPage) {
			JSONObject obj = new JSONObject();
			obj.put("title", post.getTitle());
			obj.put("score", post.getScore());
			obj.put("author", post.getAuthor());
			obj.put("URL", post.getUrl());
			
			posts.add(obj);
		}
		
		json.put("posts", posts);
		
		return json;
		
	}
}
