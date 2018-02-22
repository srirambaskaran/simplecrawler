package org.crawler.simplecrawler;

import java.util.HashSet;
import java.util.regex.Pattern;

public class CrawlerProperties {
    public static final String AGENT_NAME = "cs572 - crawler";
    public static final String CRAWLER_OUTPUT_FOLDER = "data/";
    public static final int NUM_CRAWLERS = 20;
    public static final String ALLOWED_PAGE_EXTENSIONS = ".*(\\.html|pdf|png|jpg|tiff)$";
    public static final int MAX_DEPTH = 16;
    public static final int MAX_PAGES = 20000;
    public static final boolean INCLUDE_HTTPS = true;
    public static final String[] WEBSITE_SEEDS = { "http://www.nbcnews.com/", "https://www.nbcnews.com/",
            "http://nbcnews.com/", "https://nbcnews.com/" };
    public static final Pattern WEBSITE_STARTSWITH_PATTERN = Pattern.compile("^(http(s)?\\:\\/\\/)?(www\\.)?nbcnews\\.com.*");
    public static final boolean INCLUDE_BINARY = true;
    public static final boolean FOLLOW_REDIRECTS = true;
    public static final int POLITENESS_DELAY = 1000;
    
    public static HashSet<String> visitedURLs = new HashSet<String>();
}
