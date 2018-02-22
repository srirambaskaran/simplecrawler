package org.crawler.simplecrawler;

import java.util.HashMap;

public class Statistics {
    static int GLOBAL_ID = 0;
    
    private HashMap<String, Integer> fetchedURLStatusCodes;
    private HashMap<String, ProcessedInfo> processedURLInfo;
    private HashMap<URL, Boolean> urlFromSameDomain;
    public Statistics() {
        this.fetchedURLStatusCodes = new HashMap<String, Integer>();
        this.processedURLInfo = new HashMap<String, ProcessedInfo>();
        this.urlFromSameDomain = new HashMap<URL, Boolean>();
    }
    public HashMap<String, Integer> getFetchedURLStatusCodes() {
        return fetchedURLStatusCodes;
    }
    public HashMap<String, ProcessedInfo> getProcessedURLInfo() {
        return processedURLInfo;
    }
    public HashMap<URL, Boolean> getUrlFromSameDomain() {
        return urlFromSameDomain;
    }
}

class ProcessedInfo {
    private long size;
    private int numLinks;
    private String contentType;
    public ProcessedInfo(long size, int numLinks, String contentType) {
        super();
        this.size = size;
        this.numLinks = numLinks;
        this.contentType = contentType;
    }
    public long getSize() {
        return size;
    }
    public int getNumLinks() {
        return numLinks;
    }
    public String getContentType() {
        return contentType;
    }
}

class URL{
    
    
    private int id;
    private String url;
    public URL(String url) {
        super();
        this.id = Statistics.GLOBAL_ID++;
        this.url = url;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public int hashCode() {
        return this.id;
    }
}