package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import com.cscc01.chatbot.backend.indexer.exception.IndexAlreadyExistedException;
import com.cscc01.chatbot.backend.querysystem.NauturalLanguageProcessService;
import com.cscc01.chatbot.backend.querysystem.QueryAnalyzer;
import com.cscc01.chatbot.backend.querysystem.QueryTranslator;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexerTests  {

    @Autowired
    Indexer indexer;

    private File resourcesDirectory = new File("src/main/resources");

    @Before
    @After
    public void rollback() throws IOException {
        Term term = new Term(LuceneFieldConstants.FILE_NAME.getText(), "Index.html");
        indexer.deleteDocument(term);
        Term term1 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "test.txt");
        indexer.deleteDocument(term1);
        Term term2 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "ChatBotProject.pdf");
        indexer.deleteDocument(term2);
        Term term3 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "crawler.doc");
        indexer.deleteDocument(term3);
        Term term4 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "crawler.docx");
        indexer.deleteDocument(term4);
        Term term5 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "Index.txt");
        indexer.deleteDocument(term5);
        Term term6 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "Index.pdf");
        indexer.deleteDocument(term6);
    }

    @Test
    public void testUploadHtmlFile()
            throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/Index.html");

        Term term = new Term(LuceneFieldConstants.FILE_NAME.getText(), "Index.html");
        indexer.deleteDocument(term);
        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.FILE_NAME, "Index.html");
        Assert.assertEquals(0, docs.size());
    }

    @Test
    public void testUploadSimpleTextFile()
            throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/Index.txt");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/test.txt");

        Term term1 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "test.txt");
        indexer.deleteDocument(term1);
        Term term2 = new Term(LuceneFieldConstants.FILE_NAME.getText(), "Index.txt");
        indexer.deleteDocument(term2);

        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.CONTENT, "chatbot");
        Assert.assertEquals(0, docs.size());
    }

    @Test
    public void testUploadPdfFile()
            throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/ChatBotProject.pdf");

        Term term = new Term(LuceneFieldConstants.FILE_NAME.getText(), "ChatBotProject.pdf");
        indexer.deleteDocument(term);
        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.CONTENT, "fintech");
        Assert.assertEquals(0, docs.size());
    }

    @Test
    public void testGivenFilenameSearchFile()
            throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/Index.pdf");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/Index.html");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/Index.txt");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/test.txt");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/crawler.doc");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/crawler.docx");
        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/ChatBotProject.pdf");
        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.CONTENT, "fintech ai");
        Assert.assertEquals("ChatBotProject.pdf", docs.get(0).get("filename"));
    }

    @Test
    public void testGivenContentSearchTextFile() throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {

        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/test.txt");
        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.CONTENT, "chatbot");
        Assert.assertEquals("test.txt", docs.get(0).get("filename"));
    }

    @Test
    public void testGivenContentSearchDocFile() throws IOException, ParseException,
            FileTypeNotSupportedException, TikaException, SAXException {

        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/crawler.doc");
        List<Document> docs = indexer.searchByQueryString(LuceneFieldConstants.CONTENT, "crawler");
        Assert.assertEquals("crawler.doc", docs.get(0).get("filename"));
    }

    @Test(expected = FileTypeNotSupportedException.class)
    public void testNotSupportedFileType() throws IOException,
            FileTypeNotSupportedException, TikaException, SAXException {

        indexer.createIndex(resourcesDirectory.getAbsolutePath() + "/test/notSupported.odt");

    }

}