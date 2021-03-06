package com.cscc01.chatbot.backend.crawler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);
    private Map<CrawlerResultKey, String> result;

    /**
     * Override the shouldVisit method inherited frim WebCrawler,
     * sugguesting which URL should be visited and which should be ignored
     * @param referringPage the Page object
     * @param url the WebURL object for the url waited to be determined
     * @return the boolean stands for whether or not the url should be visited
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined filters.
        if (CrawlerConfiguration.FILTERS.matcher(href).matches()) {
            return false;
        }

        // Only accept the url if it is in the "www.utsc.utoronto.ca" domain and protocol is "https".
        return true;
    }


    /**
     * Save the crawled URL's info into a HashMap
     * @param page 
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        LOGGER.info("URL: {}", url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            String title = htmlParseData.getTitle();
            result = new HashMap<>();
            result.put(CrawlerResultKey.TITLE, title);
            result.put(CrawlerResultKey.CONTENT, text);
            result.put(CrawlerResultKey.HTML, html);
            result.put(CrawlerResultKey.URL, url);

            LOGGER.debug("URL: {}", url);
            LOGGER.debug("Text length: {}", text.length());
            LOGGER.debug("Html length: {}", html.length());
        }
    }

    /**
     * Get all the info that current URL has
     * @return all the info about the webiste
     */
    public Map<CrawlerResultKey, String> getResult(){
        return result;
    }
}
