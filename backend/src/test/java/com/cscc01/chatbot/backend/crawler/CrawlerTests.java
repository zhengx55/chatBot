package com.cscc01.chatbot.backend.crawler;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CrawlerTests {

    @Autowired
    CrawlerService crawlerService;

    @Test
    public void testUTSCCrawler() throws Exception {
        String seedUrl = "https://www.utsc.utoronto.ca/home/";
        Map<CrawlerResultKey, String> scrapedResult = crawlerService.startCrawler(seedUrl);
        assertTrue(scrapedResult.get(CrawlerResultKey.CONTENT).contains("U of T"));
    }

    @Test
    public void testUTSC_givenGoogle() throws Exception {
        String seedUrl = "https://www.google.ca";
        Map<CrawlerResultKey, String> scrapedResult = crawlerService.startCrawler(seedUrl);
        assertTrue(!scrapedResult.get(CrawlerResultKey.CONTENT).contains("U of T"));
    }
}
