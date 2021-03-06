package com.cscc01.chatbot.backend.querysystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.HashMap;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QueryAnalyzerTests {

    @Inject
    private QueryAnalyzer queryAnalyzer;

    @Test
    public void testEmptyExtraction() {
        HashMap<String, Integer> a = queryAnalyzer.extractNoun("");
        Assert.assertEquals(0, a.size());
    }

    @Test
    public void testSentenceExtraction() {
        HashMap<String, Integer> a = queryAnalyzer.extractNoun("How about apple? Can I eat apple?");
        Assert.assertEquals(2, (int) a.get("apple"));
    }
}
