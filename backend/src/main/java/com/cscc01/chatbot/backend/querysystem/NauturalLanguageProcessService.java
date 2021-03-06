package com.cscc01.chatbot.backend.querysystem;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class NauturalLanguageProcessService {

    private NaturalLanguageUnderstanding naturalLanguageUnderstanding;

    public NauturalLanguageProcessService(@Value("${nlu.apikey}") String apiKey,
                                          @Value("${nlu.version}") String version) {
        IamOptions options = new IamOptions.Builder()
                .apiKey(apiKey)
                .build();
        naturalLanguageUnderstanding = new NaturalLanguageUnderstanding(version, options);

    }

    /**
     * analyze text to get key word via IBM natural language understanding
     * @param text
     * @return
     */
    public AnalysisResults analyzeKeyWords(String text) {
        KeywordsOptions keywords= new KeywordsOptions.Builder()
                .sentiment(true)
                .emotion(true)
                .limit(3)
                .build();

        Features features = new Features.Builder()
                .keywords(keywords)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();
        return response;
    }

    /**
     * sort key word result by relevance
     * @param results
     * @return
     */
    public List<KeywordsResult> sortResultByRelevance(AnalysisResults results) {
        List<KeywordsResult> keywords = results.getKeywords();
        keywords.sort(Comparator.comparing(KeywordsResult::getRelevance).thenComparing(KeywordsResult::getCount));
        Collections.reverse(keywords);
        return keywords;
    }

}
