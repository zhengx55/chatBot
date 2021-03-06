package com.cscc01.chatbot.backend.crawler;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import edu.uci.ics.crawler4j.crawler.CrawlController;

import java.util.Map;

/**
 * Inject the initializer into this class and start all the cralwer instances 
 * as a Spring Service
 */
@Service
public class CrawlerService{ 
    private CrawlController controller;

    @Inject
    private CrawlerInitializer crawlerInitializer;

    /**
     * Start the crawler and get back some details about the URL provided
     * @param url the seed url
     * @return the cralwing result in a map format
     * @throws Exception
     */
    public Map<CrawlerResultKey, String> startCrawler(String url) throws Exception {
        this.controller = crawlerInitializer.getCrawlController();
        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed(url);
        // Number of threads to use during crawling. Increasing this typically makes crawling faster. But crawling
        // speed depends on many other factors as well. You can experiment with this to figure out what number of
        // threads works best for you.
        int numberOfCrawlers = CrawlerConfiguration.NUMBER_OF_CRAWLERS;
        Crawler crawler = new Crawler();
        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<Crawler> factory = () -> crawler;

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);
        return crawler.getResult();
    }
}