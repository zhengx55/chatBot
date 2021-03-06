package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.crawler.Crawler;
import com.cscc01.chatbot.backend.crawler.CrawlerResultKey;
import com.cscc01.chatbot.backend.model.DocumentRecord;
import com.snowtide.PDF;
import com.snowtide.pdf.lucene.LucenePDFConfiguration;
import com.snowtide.pdf.lucene.LucenePDFDocumentFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;


import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;


@Component
public class DocumentRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentRetriever.class);

    /**
     *  create a Lucene Document from file
     * @param file
     * @return
     * @throws IOException
     */
    public Document getDocument(File file) throws IOException {

        Document document = new Document();

        document.add(new TextField(LuceneFieldConstants.CONTENT.getText(), new FileReader(file)));
        document.add(new StringField(LuceneFieldConstants.FILE_NAME.getText(),
                file.getName(), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_PATH.getText(),
                file.getCanonicalPath(), Field.Store.YES));
        return document;
    }

    /**
     * create a Lucene Document from converted pdf file
     * @param file
     * @return
     * @throws IOException
     * @throws TikaException
     * @throws SAXException
     */
    public Document getPdfDocument(File file) throws IOException, TikaException, SAXException {
        Document document = new Document();
        BodyContentHandler bodyContentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream fileInputStream = new FileInputStream(file);
        ParseContext pdfContext = new ParseContext();

        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(fileInputStream, bodyContentHandler, metadata, pdfContext);


        document.add(new TextField(LuceneFieldConstants.CONTENT.getText(),
                bodyContentHandler.toString(), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_NAME.getText(),
                file.getName(), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_PATH.getText(),
                file.getCanonicalPath(), Field.Store.YES));

        LOGGER.info("Successfully converted pdf " + file.getName());
        return document;
    }


    /**
     * create a Lucene Document from doc/docx file
     * @param file
     * @return
     * @throws IOException
     * @throws TikaException
     * @throws SAXException
     */
    public Document getDocDocument(File file) throws IOException, TikaException, SAXException {
        Document document = new Document();
        BodyContentHandler bodyContentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream fileInputStream = new FileInputStream(file);


        AutoDetectParser autoDetectParser = new AutoDetectParser();
        autoDetectParser.parse(fileInputStream, bodyContentHandler, metadata);

        document.add(new TextField(LuceneFieldConstants.CONTENT.getText(),
                bodyContentHandler.toString(), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_NAME.getText(),
                file.getName(), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_PATH.getText(),
                file.getCanonicalPath(), Field.Store.YES));

        LOGGER.info("Successfully converted doc/docx " + file.getName());
        return document;
    }

    /**
     * create a Lucene Document from crawler result
     * @param crawlerResult
     * @return
     */
    public Document getCrawlerDocument(Map<CrawlerResultKey, String> crawlerResult) {
        Document document = new Document();

        document.add(new TextField(LuceneFieldConstants.CONTENT.getText(),
                crawlerResult.get(CrawlerResultKey.CONTENT), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_NAME.getText(),
                crawlerResult.get(CrawlerResultKey.TITLE), Field.Store.YES));
        document.add(new StringField(LuceneFieldConstants.FILE_PATH.getText(),
                crawlerResult.get(CrawlerResultKey.URL), Field.Store.YES));

        LOGGER.info("Successfully converted link:"
                + crawlerResult.get(CrawlerResultKey.TITLE) + ", " + crawlerResult.get(CrawlerResultKey.URL));
        return document;
    }


    /**
     * create a DocumentRecord from file so that it can be inserted into db
     * @param file
     * @param lastModifiedUser
     * @param documentId
     * @return
     */
    public static DocumentRecord buildDocumentRecord(File file,
                                                     String lastModifiedUser,
                                                     String documentId) {
        DocumentRecord newDocument = new DocumentRecord(file.getName());
        newDocument.setLastModified(getTimestamp().toString());
        newDocument.setDiscoveryId(documentId);
        newDocument.setLastModifiedUser(lastModifiedUser);
        return newDocument;
    }

    /**
     * create a DocumentRecord from crawlerResult so that it can be inserted into db
     * @param crawlerResult
     * @param lastModifiedUser
     * @param documentId
     * @return
     */
    public static DocumentRecord buildDocumentRecord(Map<CrawlerResultKey, String> crawlerResult,
                                                     String lastModifiedUser,
                                                     String documentId) {


        DocumentRecord newDocument = new DocumentRecord(crawlerResult.get(CrawlerResultKey.TITLE));
        newDocument.setLastModified(getTimestamp().toString());
        newDocument.setDiscoveryId(documentId);
        newDocument.setLastModifiedUser(lastModifiedUser);
        return newDocument;
    }

    /**
     * get current timestamp
     * @return Timestamp
     */
    private static Timestamp getTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        return timestamp;
    }


}
