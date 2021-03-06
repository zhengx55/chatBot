package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.crawler.CrawlerResultKey;
import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import com.cscc01.chatbot.backend.indexer.exception.IndexAlreadyExistedException;
import com.cscc01.chatbot.backend.model.DocumentRecord;
import com.cscc01.chatbot.backend.sql.repositories.DocumentRecordRepository;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Indexer {

    private final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);
    private final String INDEX_DIR_PATH = "./indexBase";


    @Inject
    private DocumentRetriever documentRetriever;

    @Inject
    private FileValidator fileValidator;

    private IndexWriter indexWriter;


    /**
     * Given a file path, index the file
     *
     * @param filePath
     * @throws IOException
     * @throws FileTypeNotSupportedException
     * @throws TikaException
     * @throws SAXException
     * @throws IndexAlreadyExistedException
     */
    public void createIndex(String filePath)
            throws IOException, FileTypeNotSupportedException,
            TikaException, SAXException {
        Path path = Paths.get(filePath);
        File file = path.toFile();

        createIndex(file);
    }

    /**
     * index a file to Lucene
     * @param file
     * @throws IOException
     * @throws FileTypeNotSupportedException
     * @throws TikaException
     * @throws SAXException
     */
    public void createIndex(File file)
            throws IOException, FileTypeNotSupportedException,
            TikaException, SAXException {

        IndexWriter indexWriter = getIndexWriter();
        LOGGER.info("Opening file " + file.getName() + " to create index");

        Document document = null;
        if (fileValidator.isPDF(file)) {
            document = documentRetriever.getPdfDocument(file);
            indexWriter.updateDocument(new Term(LuceneFieldConstants.FILE_NAME.getText(), file.getName()), document);
        } else if (fileValidator.isDoc(file)) {
            document = documentRetriever.getDocDocument(file);
            indexWriter.updateDocument(new Term(LuceneFieldConstants.FILE_NAME.getText(), file.getName()), document);
        } else if (fileValidator.isValidFile(file)) {
            document = documentRetriever.getDocument(file);
            indexWriter.updateDocument(new Term(LuceneFieldConstants.FILE_NAME.getText(), file.getName()), document);
        }
        indexWriter.commit();
        indexWriter.close();

        LOGGER.info("DocumentRecord " + file.getName() + " added successfully");
    }

    /**
     * create document method specific for html upload(crawler)
     *
     * @param
     * @throws IOException
     * @throws FileTypeNotSupportedException
     * @throws TikaException
     * @throws SAXException
     */
    public void createIndex(Map<CrawlerResultKey, String> crawlerResult) throws IOException {

        IndexWriter indexWriter = getIndexWriter();
        LOGGER.info("Opening link " + crawlerResult.get(CrawlerResultKey.URL) + " to create index");

        Document document = documentRetriever.getCrawlerDocument(crawlerResult);
//        DocumentRecord documentRecord = toDocumentRecord(document);
        indexWriter.updateDocument(
                new Term(LuceneFieldConstants.FILE_NAME.getText(),
                        crawlerResult.get(CrawlerResultKey.TITLE)), document);

        indexWriter.commit();
        indexWriter.close();
//        documentRecordRepository.save(documentRecord);
        LOGGER.info("DocumentRecord " + crawlerResult.get(CrawlerResultKey.TITLE) + " added successfully");
    }


    /**
     * delete a DocumentRecord from indexes, should use field LuceneField filename to create the term,
     * otherwise you may delete the wrong document by mistake.
     *
     * @param term
     * @throws IOException
     */
    public void deleteDocument(Term term) throws IOException {
        IndexWriter indexWriter = getIndexWriter();
        indexWriter.deleteDocuments(term);
        indexWriter.close();
    }

    /**
     * delete a DocumentRecord from indexes by filename
     *
     * @param filename
     * @throws IOException
     */
    public void deleteDocument(String filename) throws IOException {
        Term term = new Term(LuceneFieldConstants.FILE_NAME.getText(), filename);
        deleteDocument(term);
        LOGGER.info("Deleted document from indexer: " + filename);
    }


    /**
     * Search indexes given a LuceneField and queryString which contains keywords.
     * returns top 10 hitcount documents.
     *
     * @param fieldConstant
     * @param queryString
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<Document> searchByQueryString(LuceneFieldConstants fieldConstant, String queryString)
            throws IOException, ParseException {
        Query query = new QueryParser(fieldConstant.getText(), new EnglishAnalyzer()).parse(queryString);
        return searchByQuery(query);
    }

    /**
     * search document by a given query
     * @param query
     * @return
     * @throws IOException
     */
    public List<Document> searchByQuery(Query query) throws IOException {
        List<Document> result = new ArrayList<>();
        DirectoryReader indexReader = getIndexReader();
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopDocs topDocs = indexSearcher.search(query, 10);
        LOGGER.info("Finished searchByQueryString query " + query.toString() + "  Total Hits: " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            result.add(indexSearcher.doc(scoreDoc.doc));
        }

        indexReader.close();
        return result;
    }

    /**
     * get a Lucene indexWriter
     * @return
     * @throws IOException
     */
    private IndexWriter getIndexWriter() throws IOException {
        if (indexWriter != null && indexWriter.isOpen()) {
            indexWriter.close();
        }
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new EnglishAnalyzer());
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIR_PATH));
        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        return indexWriter;
    }

    /**
     * get a Lucene indexReader
     * @return
     * @throws IOException
     */
    private DirectoryReader getIndexReader() throws IOException {
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIR_PATH));
        DirectoryReader indexReader = DirectoryReader.open(indexDirectory);
        return indexReader;
    }

//    private DocumentRecord toDocumentRecord(Document document) {
//        DocumentRecord documentRecord = new DocumentRecord(document.get("filename"));
//        return documentRecord;
//    }
}
