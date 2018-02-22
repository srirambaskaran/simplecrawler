package org.crawler.simplecrawler;

import java.util.Set;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class NewsCrawler extends WebCrawler {
    private final static Pattern NOT_INCLUDE_EXTENSIONS = Pattern.compile(".*(\\.(css|js|mp3|mp3|zip|gz))$");
    private final static Pattern INCLUDE_EXTENSIONS = Pattern.compile(".*(\\.(html|pdf|gif|jpg|jpeg|tiff|png|JPG|JPEG|TIFF|PNG|PDF|HTML))?$");
    private final static Pattern ACCEPTED_CONTENT_TYPES = Pattern.compile("^.*(\\s?\\/\\s?(html|pdf|png|jpeg|tiff|gif)).*");
    
    private Statistics stats;
    
    public NewsCrawler() {
        this.stats = new Statistics();
    }
    
    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        String url = webUrl.getURL().replace(",", "_");
        this.stats.getFetchedURLStatusCodes().put(url, statusCode);
    }
    
    

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        if(!ACCEPTED_CONTENT_TYPES.matcher(page.getContentType()).matches())
            return;
        if (page.getParseData() instanceof HtmlParseData) {
            
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            long size = htmlParseData.getHtml().getBytes().length / 1024;
            String contentType = page.getContentType().split(";")[0];
            int links = htmlParseData.getOutgoingUrls().size();
            addURLs(htmlParseData.getOutgoingUrls());
            ProcessedInfo info = new ProcessedInfo(size, links, contentType);
            this.stats.getProcessedURLInfo().put(url, info);
            
        } else if (page.getParseData() instanceof BinaryParseData){
            long size = page.getContentData().length / 1024;
            String contentType = page.getContentType();
            BinaryParseData binaryParsed = (BinaryParseData) page.getParseData();
            int links = 0;
            if(binaryParsed != null) {
                links = binaryParsed.getOutgoingUrls() != null ? binaryParsed.getOutgoingUrls().size() : 0;
                addURLs(binaryParsed.getOutgoingUrls());
            }
            ProcessedInfo info = new ProcessedInfo(size, links, contentType);
            this.stats.getProcessedURLInfo().put(url, info);
        }
        
        
    }
    
    private void addURLs(Set<WebURL> urls) {
        for(WebURL url : urls)
            stats.getUrlFromSameDomain().put(new URL(url.getURL()), isFromSameDomain(url.getURL()));
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        boolean doNotAdd = false;
        synchronized (href) {
            if(CrawlerProperties.visitedURLs.contains(href))
                doNotAdd = true;
            else
                CrawlerProperties.visitedURLs.add(href);
        }
        return !doNotAdd 
                && !NOT_INCLUDE_EXTENSIONS.matcher(href).matches() 
                && INCLUDE_EXTENSIONS.matcher(href).matches()
                && CrawlerProperties.WEBSITE_STARTSWITH_PATTERN.matcher(href).find();
    }

    private Boolean isFromSameDomain(String href) {
        return CrawlerProperties.WEBSITE_STARTSWITH_PATTERN.matcher(href).matches();
    }
    
    public Statistics getStats() {
        return this.stats;
    }
    

}
