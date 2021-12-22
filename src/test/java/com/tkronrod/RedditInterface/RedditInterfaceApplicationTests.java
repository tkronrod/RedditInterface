package com.tkronrod.RedditInterface;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedditInterfaceApplicationTests {
	
	static RedditReader reddit;
	
	
	@BeforeAll
	static void init() {
		reddit = new RedditReader();
		reddit.start();
	}
	
	@Test
	void health() {
		assertTrue(reddit.health());
	}
	
	@Test
	void testFrontPage() {
		List<String> titles = reddit.getFrontPage();
		
		assertTrue(titles.size() == 100);
	}
	
	@Test
	void certainSubreddit() throws JSONException {
		JSONObject json = reddit.getSubredditTop("aww");
		
		JSONArray postsArray = (JSONArray) json.get("posts");
		
		Iterator<Object> posts = postsArray.iterator();
		
		while(posts.hasNext()) {
			JSONObject post = (JSONObject) posts.next();
			
			assertTrue(post.get("title") != null);
			assertTrue(post.get("author") != null);
			assertTrue(post.get("score") != null);
			assertTrue(post.get("URL") != null);
		}
	}
	
	@Test
	void nonExistentSubreddit() {
		JSONObject json = reddit.getSubredditTop("asdfasdfasdfasdfasdfsa");
		
		assertTrue(json.length() == 0);
	}
	
	@Test
	void emptyString() {
		JSONObject json = reddit.getSubredditTop("");
		
		assertTrue(json.length() == 0);
	}
	
	@Test
	void nullPass() {
		JSONObject json = reddit.getSubredditTop(null);
		
		assertTrue(json.length() == 0);
	}
	
	@Test
	void random() {
		JSONObject json = reddit.getSubredditTop("fjkdlsfjdsalkfjdsal");
		
		assertTrue(json.length() == 0);
	}

}
