package org.crawler.simplecrawler;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.opencsv.CSVReader;

public class ProcessStats {
    public static void main(String[] args) throws IOException {
        String fetchCSV = "fetch_NBC_News.csv";
        String urlCSV = "urls_NBC_News.csv";
        String visitCSV = "visit_NBC_News.csv";
        
        fetchFileStats(fetchCSV);
        System.out.println();
        visitFileStats(visitCSV);
        urlStats(urlCSV);
    }
    
    private static void urlStats(String urlCSV) throws IOException{
        
        CSVReader reader = new CSVReader(new FileReader(urlCSV));
        HashMap<String, Integer> insideOutsideMap = new HashMap<>();
        HashMap<String, String> urlMap = new HashMap<>();
        String[] line = null;
        int numLines = 0;
        while((line = reader.readNext())!=null) {
            numLines ++;
            urlMap.put(line[0], line[1]);
        }
        
        for(String url : urlMap.keySet()) {
            insideOutsideMap.put(urlMap.get(url), insideOutsideMap.getOrDefault(urlMap.get(url), 0) + 1);
        }
        
        System.out.println("Outgoing URLs:\n" + 
                "==============");
        System.out.println("Total URLs extracted: "+numLines);
        System.out.println("# unique URLs extracted: "+urlMap.size());
        System.out.println("# unique URLs within News Site: "+insideOutsideMap.get("OK"));
        System.out.println("# unique URLs outside News Site: "+insideOutsideMap.get("N_OK"));
        System.out.println();
        
        reader.close();
    }
    
    private static void fetchFileStats(String fetchCSV) throws IOException {
        HashMap<String, Integer> statusCodeClassification = new HashMap<>();
        HashMap<String, Integer> fetchedClassification = new HashMap<>();
        int numFetchLines = 0;
        CSVReader reader = new CSVReader(new FileReader(fetchCSV));
        String[] line = null;
        while((line = reader.readNext())!=null) {
            numFetchLines ++ ;
            if(statusCodeClassification.containsKey(line[1]))
                statusCodeClassification.put(line[1], statusCodeClassification.get(line[1]) + 1);
            else
                statusCodeClassification.put(line[1], 1);
            
            if(line[1].equals("200")) {
                fetchedClassification.put("# fetches succeeded: ",fetchedClassification.getOrDefault("# fetches succeeded: ", 0) + 1);
            } else {
                fetchedClassification.put("# fetches failed or aborted: ",fetchedClassification.getOrDefault("# fetches failed or aborted: ", 0) + 1);
            }
        }
        
        System.out.println("Fetch Statistics\n" + "================");
        System.out.println("# fetches attempted: "+numFetchLines);
        System.out.println("# fetches succeeded: "+fetchedClassification.get("# fetches succeeded: "));
        System.out.println("# fetches failed or aborted: "+fetchedClassification.get("# fetches failed or aborted: "));
        System.out.println();
        
        System.out.println("Status Codes:\n" + "=============");
        for(String status : statusCodeClassification.keySet()) {
            System.out.println(status+" : "+statusCodeClassification.get(status));
        }
        
        reader.close();
    }
    
    private static void visitFileStats(String visitCSV) throws IOException {
        HashMap<String, Integer> contentTypeClassification = new HashMap<>();
        HashMap<String, Integer> sizeClassification = new HashMap<>();
        int numVisitLines = 0;
        CSVReader reader = new CSVReader(new FileReader(visitCSV));
        String[] line = null;
        while((line = reader.readNext())!=null) {
            numVisitLines += Integer.parseInt(line[2]);
            contentTypeClassification.put(line[3], contentTypeClassification.getOrDefault(line[3], 0) + 1);
            int size = Integer.parseInt(line[1]);
            if(size < 1)
                sizeClassification.put("< 1KB", sizeClassification.getOrDefault("< 1KB",0) + 1);
            else if(size >= 1 && size < 10)
                sizeClassification.put(">= 1KB ~ < 10KB", sizeClassification.getOrDefault(">= 1KB ~ < 10KB",0) + 1);
            else if(size >= 10 && size < 100)
                sizeClassification.put(">= 10KB ~ < 100KB", sizeClassification.getOrDefault(">= 10KB ~ < 100KB",0) + 1);
            else if(size >= 100 && size < 1024)
                sizeClassification.put(">= 100KB ~ < 1MB", sizeClassification.getOrDefault(">= 100KB ~ < 1MB",0) + 1);
            else
                sizeClassification.put(">= 1MB", sizeClassification.getOrDefault(">= 1MB",0) + 1);
        }
        
        System.out.println("Content Types:\n" + "================");
        for(String type : contentTypeClassification.keySet()) {
            System.out.println(type+": "+contentTypeClassification.get(type));
        }
        
        System.out.println();
        
        System.out.println("Status Codes:\n" + "=============");
        for(String size : sizeClassification.keySet()) {
            System.out.println(size+": "+sizeClassification.get(size));
        }
        
        System.out.println("Visit Lines: "+numVisitLines);
        
        reader.close();
    }
    
}
