package com.tkronrod.RedditInterface;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Responsible for processing the HTTP requests that the app receives
 * 
 * @author talkronrod
 *
 */
@RestController
public class RedditController {

	private RedditReader reddit;
	
	/**
	 * Constructs the API interface 
	 */
	public RedditController() {
		reddit = new RedditReader();
		reddit.start();
	}
	
	/**
	 * Processes the HTTP request to get the top posts from a specific subreddit and returns 
	 * a JSON object containing the response
	 * 
	 * @param subreddit the name of the subreddit (without the 'r/' prefix)
	 * @return a JSON containing a list of the top posts
	 */
	@GetMapping(path = "/reddit/{subreddit}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> topList(@PathVariable String subreddit) {
		return new ResponseEntity<Object>(reddit.getSubredditTop(subreddit).toString(), HttpStatus.OK);
	}
}
