package com.cscc01.chatbot.backend.crawler;

import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * This initializer exposed as a Spring Component
 */
@Component
public class CrawlerInitializer {

    /**
     *  This method configures all the properties that the cralwercontroller needs to have
     * @return the configured CrawlerController
     * @throws Exception
     */
    public CrawlController getCrawlController() throws Exception {
        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls
        // that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder(CrawlerConfiguration.STORAGE_FOLDER);

        // Be polite: Make sure that we don't send more than 1 request per second (1000
        // milliseconds between requests).
        // Otherwise it may overload the target servers.
        config.setPolitenessDelay(CrawlerConfiguration.POLITENESS_DELAY);

        // You can set the maximum crawl depth here. The default value is -1 for
        // unlimited depth.
        config.setMaxDepthOfCrawling(CrawlerConfiguration.MAX_DEPTH_CRAWLING);

        // You can set the maximum number of pages to crawl. The default value is -1 for
        // unlimited number of pages.
        config.setMaxPagesToFetch(CrawlerConfiguration.MAX_PAGES_FETCH);

        // Should binary data should also be crawled? example: the contents of pdf, or
        // the metadata of images etc
        config.setIncludeBinaryContentInCrawling(CrawlerConfiguration.INCLUDE_BINARY_CONTENT);

        // This config parameter can be used to set your crawl to be resumable
        // (meaning that you can resume the crawl from a previously
        // interrupted/crashed crawl). Note: if you enable resuming feature and
        // want to start a fresh crawl, you need to delete the contents of
        // rootFolder manually.
        config.setResumableCrawling(CrawlerConfiguration.RESUMABLE_CRAWLING);


        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        return new CrawlController(config, pageFetcher, robotstxtServer); 
    }
}
