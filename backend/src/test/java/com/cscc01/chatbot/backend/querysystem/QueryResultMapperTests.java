package com.cscc01.chatbot.backend.querysystem;

import com.cscc01.chatbot.backend.indexer.LuceneFieldConstants;
import com.cscc01.chatbot.backend.model.QueryResult;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryResultMapperTests {

    @Test
    public void testIndexerResultMapping() {
        Document document = new Document();

        document.add(new TextField(LuceneFieldConstants.CONTENT.getText(), "content", Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_NAME.getText(), "file", Field.Store.YES));

        QueryResult queryResult = QueryResultMapper.fromIndexerResult(document);
        Assert.assertEquals("file", queryResult.getFilename());
        Assert.assertEquals("content",queryResult.getContent());

    }

    @Test
    public void testDiscoveryResultMapping() {
        QueryResponse response = Mockito.mock(QueryResponse.class);
        com.ibm.watson.discovery.v1.model.QueryResult queryResult1
                = Mockito.mock(com.ibm.watson.discovery.v1.model.QueryResult.class);

        Mockito.when(queryResult1.get("text")).thenReturn("content");
        Mockito.when(queryResult1.getTitle()).thenReturn("file");
        Mockito.when(response.getResults()).thenReturn(Arrays.asList(queryResult1));

        QueryResult queryResult = QueryResultMapper.fromDiscoveryResult(response);
        Assert.assertEquals("file", queryResult.getFilename());
        Assert.assertEquals("content",queryResult.getContent());

    }
}
