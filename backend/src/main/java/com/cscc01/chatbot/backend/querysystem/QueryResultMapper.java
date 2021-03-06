package com.cscc01.chatbot.backend.querysystem;

import com.cscc01.chatbot.backend.indexer.LuceneFieldConstants;
import com.cscc01.chatbot.backend.model.QueryResult;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import org.apache.lucene.document.Document;

public class QueryResultMapper {

    /**
     * map a Lucene Document to query result
     * @param document
     * @return
     */
    public static QueryResult fromIndexerResult(Document document) {
        return new QueryResult.Builder()
                .content(document.get(LuceneFieldConstants.CONTENT.getText()))
                .filename(document.get(LuceneFieldConstants.FILE_NAME.getText()))
                .build();

    }

    /**
     * map a discovery response to query result
     * @param response
     * @return
     */
    public static QueryResult fromDiscoveryResult(QueryResponse response) {
        return new QueryResult.Builder()
                .content(response.getResults().get(0).get("text").toString())
                .filename(response.getResults().get(0).getTitle())
                .build();
    }
}
