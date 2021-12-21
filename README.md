# RedditInterface

RedditInterface is a Java web app that integrates with Reddit's API to show top articles in a given subreddit. 

## Local Installation and Use

Download and run the `jar` file in your command line. It is possible that since the program uses port 80 for processing the HTTP request the program will fail if its not run using `sudo`.

To make an HTTP request either install the provided Postman profile and replace the subreddit name (by default is "aww") with the desired subreddit. 
You can also send your own HTTP request to `localhost:80/reddit/subredditNameHere`.

## Remote Access

Access the current build of the app remotely by connecting to `18.223.120.103/reddit/subredditNameHere`.

## Frameworks

The app runs on Java 11 and uses the Springboot framework for application bootstrap web capabilities. The app also uses the [JRAW](https://github.com/mattbdean/JRAW) Java Reddit API wrapper to interface with reddit and make GET requests. The app replies to HTTP requests with JSONs and uses the [JSONObject](https://www.javadoc.io/doc/org.json/json/20170516/org/json/JSONObject.html) framework to create and pass along JSONs. 

## Overview

The app runs on a Springboot instance that listens to incoming HTTP requests (on port 80), and once it receives one it passes it along to the RedditReader class which sends a GET request to the Reddit API for the top posts of the requested subreddit. It then gathers the usesful information on the top 100 articles in that subreddit and returns them in a list in a JSON. 
Since Springboot starts a separate thread for each incoming request, the app can accept and process multiple requests at the same time. 
There is an instance of the app running on AWS EC2, the directions for which are above in the *Remote Access* section. 