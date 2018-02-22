package org.crawler.simplecrawler;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

import com.opencsv.CSVWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    
    public static void main(String[] args) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(CrawlerProperties.CRAWLER_OUTPUT_FOLDER);
        config.setMaxDepthOfCrawling(CrawlerProperties.MAX_DEPTH);
        config.setMaxPagesToFetch(CrawlerProperties.MAX_PAGES);
        config.setUserAgentString(CrawlerProperties.AGENT_NAME);
        config.setIncludeHttpsPages(CrawlerProperties.INCLUDE_HTTPS);
        config.setPolitenessDelay(CrawlerProperties.POLITENESS_DELAY);
        config.setIncludeBinaryContentInCrawling(CrawlerProperties.INCLUDE_BINARY);
        config.setFollowRedirects(CrawlerProperties.FOLLOW_REDIRECTS);
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        for (String seed : CrawlerProperties.WEBSITE_SEEDS) {
            controller.addSeed(seed);
        }
        
        CrawlerFactory factory = new CrawlerFactory();
        controller.start(factory, CrawlerProperties.NUM_CRAWLERS);
        controller.waitUntilFinish();
        
        
        HashSet<NewsCrawler> crawlers = factory.getCrawlerInstances();
        
        CSVWriter fetchFile = new CSVWriter(new FileWriter("fetch_NBC_News.csv"));
        CSVWriter successfulDownloadsFile = new CSVWriter(new FileWriter("visit_NBC_News.csv"));
        CSVWriter discoveredURLFile = new CSVWriter(new FileWriter("urls_NBC_News.csv"));
        
        for(NewsCrawler crawler: crawlers) {
            Statistics crawlerStats = crawler.getStats();
            HashMap<String, Integer> fetchedURLs = crawlerStats.getFetchedURLStatusCodes();
            HashMap<String, ProcessedInfo> successfulURLs = crawlerStats.getProcessedURLInfo();
            HashMap<URL, Boolean> sameDomianURLs = crawlerStats.getUrlFromSameDomain();
            //Fetch
            for(String url : fetchedURLs.keySet()) {
                String[] row = {url, fetchedURLs.get(url)+""};
                fetchFile.writeNext(row);
            }
            
            //Successful Downloads
            for(String url : successfulURLs.keySet()) {
                ProcessedInfo info =  successfulURLs.get(url);
                String[] row = {url, info.getSize()+"", info.getNumLinks()+"", info.getContentType()};
                successfulDownloadsFile.writeNext(row);
            }
            
            //Discovered URLs
            for(URL url : sameDomianURLs.keySet()) {
                String[] row = {url.getUrl(), sameDomianURLs.get(url)?"OK":"N_OK"+""};
                discoveredURLFile.writeNext(row);
            }
        }
        
        fetchFile.close();
        successfulDownloadsFile.close();
        discoveredURLFile.close();
        
        
        
    }
}
