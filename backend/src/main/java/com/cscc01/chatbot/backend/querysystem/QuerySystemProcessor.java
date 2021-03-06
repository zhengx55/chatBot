package com.cscc01.chatbot.backend.querysystem;

import com.cscc01.chatbot.backend.indexer.Indexer;
import com.cscc01.chatbot.backend.indexer.WatsonDiscovery;
import com.cscc01.chatbot.backend.model.QueryResult;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeIntent;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuerySystemProcessor {

    @Inject
    NauturalLanguageProcessService nauturalLanguageProcessService;

    @Inject
    QueryAnalyzer queryAnalyzer;

    @Inject
    WatsonDiscovery watsonDiscovery;

    @Inject
    WatsonAssistant watsonAssistant;

    @Inject
    Indexer indexer;


    /**
     * query IBM assistant and discovery( if the user intent is askDiscovery) to get a response(analyze the user input),
     * @param textMsg user input
     * @return response message map
     * @throws IOException
     * @throws ParseException
     */
    public Map<String, Object> getResponse(String textMsg) throws IOException, ParseException {
        Map<String, Object> chatBotResponse = new HashMap<>();
        Assistant assistantInstance = watsonAssistant.createNewAssistant();
        SessionResponse newSession = watsonAssistant.createSession(assistantInstance);
        MessageResponse response = watsonAssistant.getMessageResponse(textMsg, newSession, assistantInstance);
        List<RuntimeIntent> intents = response.getOutput().getIntents();

        if (intents.size() > 0) {
            if (intents.get(0).getIntent().equals("askDiscovery")) {
                QueryResult queryResult = query(textMsg);
                chatBotResponse.put("content", queryResult.getContent());
            } else {
                chatBotResponse.put("message", response.getOutput().getGeneric().get(0).getText());
            }
        } else {
            chatBotResponse.put("message", response.getOutput().getGeneric().get(0).getText());
        }

        return chatBotResponse;
    }

    /**
     * according to user input query discovery for a document,
     * if discovery fails to response, query Lucene
     * @param text
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public QueryResult query(String text) throws ParseException, IOException {
        boolean discoveryFailedToQuery = false;
        QueryResponse discoveryResult = null;
        try {
            discoveryResult = watsonDiscovery.query(text);
            if (discoveryResult.getResults().size() == 0) {
                discoveryFailedToQuery = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            discoveryFailedToQuery = true;
        }
        List<Document> indexerResult = null;

        if (discoveryFailedToQuery) {
            Query query;
            try {
                AnalysisResults results = nauturalLanguageProcessService.analyzeKeyWords(text);
                query = QueryTranslator.fromKeyword(nauturalLanguageProcessService.sortResultByRelevance(results));
            } catch (Exception e) {
                HashMap<String, Integer> keywordResult = queryAnalyzer.extractNoun(text);
                query = QueryTranslator.fromMap(keywordResult);
            }
            indexerResult = indexer.searchByQuery(query);
        }

        QueryResult result = null;
        if (discoveryResult != null) {
            result = QueryResultMapper.fromDiscoveryResult(discoveryResult);
        } else if (indexerResult != null) {
            result = QueryResultMapper.fromIndexerResult(indexerResult.get(0));
        }

        return result;
    }
}
