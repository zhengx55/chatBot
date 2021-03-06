package com.cscc01.chatbot.backend.querysystem;

import com.cscc01.chatbot.backend.model.QueryResult;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsResult;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryTranslatorTests {


    @Test
    public void tesMapResultMapping() throws ParseException {
        Map<String, Integer> map = new HashMap<>();
        map.put("R1", 2);

        Query query = QueryTranslator.fromMap(map);
        Assert.assertEquals("contents:r1", query.toString());

    }


    @Test
    public void testKeywordResultMapping() throws ParseException {
        KeywordsResult keywordsResult1 = Mockito.mock(KeywordsResult.class);
        KeywordsResult keywordsResult2 = Mockito.mock(KeywordsResult.class);
        KeywordsResult keywordsResult3 = Mockito.mock(KeywordsResult.class);

        Mockito.when(keywordsResult1.getText()).thenReturn("R1");
        Mockito.when(keywordsResult2.getText()).thenReturn("R2");
        Mockito.when(keywordsResult3.getText()).thenReturn("r3");


        Query query = QueryTranslator.fromKeyword(Arrays.asList(keywordsResult1, keywordsResult2, keywordsResult3));
        Assert.assertEquals("contents:r1 contents:r2 contents:r3", query.toString());

    }
}
