package com.cscc01.chatbot.backend.querysystem;

import com.cscc01.chatbot.backend.querysystem.nlpmodel.NlpModel;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class QueryAnalyzer {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private ChunkerME chunker;
    private PorterStemmer stemmer;

    private List<String> stopwords;


    public QueryAnalyzer(NlpModel nlpModel) throws IOException {
        stopwords =  IOUtils.readLines(new ClassPathResource("StopWords.txt").getInputStream());
        sentenceDetector = new SentenceDetectorME(nlpModel.sentenceModel);
        tokenizer = new TokenizerME(nlpModel.tokenizerModel);
        posTagger = new POSTaggerME(nlpModel.posModel);
        chunker = new ChunkerME(nlpModel.chunkerModel);
        stemmer = new PorterStemmer();
    }

    public String[] getSentences(String text) {
        return sentenceDetector.sentDetect(text);
    }

    public void cleanText(String text) {
        stemmer.reset();
        stemmer.stem(text);
        System.out.println(stemmer.getResultBuffer());
    }


    /**
     * extract noun word from text
     * @param sentence
     * @return
     */
    public HashMap<String, Integer> extractNoun(String sentence) {


        HashMap<String, Integer> termFrequencies = new HashMap<>();

        String[] words = tokenizer.tokenize(sentence.toLowerCase());
        String[] posTags = posTagger.tag(words);
        Span[] chunks = chunker.chunkAsSpans(words, posTags);
        String[] chunkStrings = Span.spansToStrings(chunks, words);

        for (int i = 0; i < chunks.length; i++) {
            String np = chunkStrings[i];
            if (chunks[i].getType().equals("NP") && !stopwords.contains(np)) {
                if (termFrequencies.containsKey(np)) {
                    termFrequencies.put(np, termFrequencies.get(np) + 1);
                } else {
                    termFrequencies.put(np, 1);
                }
            }
        }
        return termFrequencies;
    }
}