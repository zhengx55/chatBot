package com.cscc01.chatbot.backend.querysystem.nlpmodel;

import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class NlpModel {

    private final InputStream SENTENCE_MODEL = new ClassPathResource("model/en-sent.bin").getInputStream();
    private final InputStream CHUNKER_MODEL = new ClassPathResource("model/en-chunker.bin").getInputStream();
    private final InputStream TOKENIZER_MODEL = new ClassPathResource("model/en-token.bin").getInputStream();
    private final InputStream POSTAGGER_MODEL = new ClassPathResource("model/en-pos-maxent.bin").getInputStream();
    private final InputStream PARSER_MODEL = new ClassPathResource("model/en-parser-chunking.bin").getInputStream();

    public SentenceModel sentenceModel;
    public TokenizerModel tokenizerModel;
    public POSModel posModel;
    public ChunkerModel chunkerModel;
    public ParserModel parserModel;

    public NlpModel() throws IOException {
        sentenceModel = new SentenceModel(SENTENCE_MODEL);
        tokenizerModel = new TokenizerModel(TOKENIZER_MODEL);
        posModel = new POSModel(POSTAGGER_MODEL);
        chunkerModel = new ChunkerModel(CHUNKER_MODEL);
        parserModel = new ParserModel(PARSER_MODEL);
    }
}
