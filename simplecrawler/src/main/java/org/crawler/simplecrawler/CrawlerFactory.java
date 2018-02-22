package org.crawler.simplecrawler;

import java.util.HashSet;

import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;

public class CrawlerFactory implements WebCrawlerFactory<NewsCrawler> {
    
    private HashSet<NewsCrawler> crawlerInstances; 
    
    public CrawlerFactory() {
        this.crawlerInstances = new HashSet<NewsCrawler>();
    }

    public NewsCrawler newInstance() throws Exception {
        NewsCrawler newsCrawler = new NewsCrawler();
        this.crawlerInstances.add(newsCrawler);
        return newsCrawler; 
    }

    public HashSet<NewsCrawler> getCrawlerInstances() {
        return crawlerInstances;
    }
    
    

}
