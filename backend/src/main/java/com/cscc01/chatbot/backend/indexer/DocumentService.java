package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.crawler.CrawlerResultKey;
import com.cscc01.chatbot.backend.crawler.CrawlerService;
import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import com.cscc01.chatbot.backend.model.DocumentRecord;
import com.cscc01.chatbot.backend.model.User;
import com.cscc01.chatbot.backend.sql.repositories.DocumentRecordRepository;
import com.ibm.watson.discovery.v1.model.DocumentAccepted;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {

    @Inject
    private DocumentRecordRepository documentRecordRepository;

    @Inject
    private Indexer indexer;

    @Inject
    private CrawlerService crawlerService;

    @Inject
    private WatsonDiscovery watsonDiscovery;

    /**
     * index a file to watson Discovery and Lucene
     *  and insert a documentRecord to db
     * @param file
     * @throws IOException
     * @throws TikaException
     * @throws FileTypeNotSupportedException
     * @throws SAXException
     */
    public void addFileDocument(File file, String user) throws IOException, TikaException, FileTypeNotSupportedException, SAXException {
        DocumentRecord documentRecord = documentRecordRepository.findByName(file.getName());
        String documentId = UUID.randomUUID().toString();
        if (documentRecord != null) {
            if (documentRecord.getDiscoveryId() != null) {
                documentId = documentRecord.getDiscoveryId();
            }
        }
        DocumentAccepted result = watsonDiscovery.addDocument(file, documentId);
        indexer.createIndex(file);
        if (result.getDocumentId() != null) {
            documentId = result.getDocumentId();
        }
        documentRecord = DocumentRetriever.buildDocumentRecord(file, user, documentId);
        if (documentRecordRepository.findByName(documentRecord.getName()) != null) {
            documentRecordRepository.delete(documentRecord);
        }
        documentRecordRepository.save(documentRecord);
    }

    /**
     * crawl a url and index it to watson Discovery and Lucene,
     * and insert a documentRecord to db
     * @param url
     * @return
     * @throws Exception
     */
    public String addUrlDocument(String url, String user) throws Exception {
        Map<CrawlerResultKey, String> crawlerResult = crawlerService.startCrawler(url);
        String filename = crawlerResult.get(CrawlerResultKey.TITLE);
        DocumentRecord documentRecord = documentRecordRepository.findByName(filename);
        String documentId = UUID.randomUUID().toString();
        if (documentRecord != null) {
            if (documentRecord.getDiscoveryId() != null) {
                documentId = documentRecord.getDiscoveryId();
            }
        }

        DocumentAccepted result = watsonDiscovery.addDocument(crawlerResult, documentId);
        indexer.createIndex(crawlerResult);

        if (result.getDocumentId() != null) {
            documentId = result.getDocumentId();
        }

        documentRecord = DocumentRetriever.buildDocumentRecord(crawlerResult, user, documentId);
        if (documentRecordRepository.findByName(documentRecord.getName()) != null) {
            documentRecordRepository.delete(documentRecord);
        }
        documentRecordRepository.save(documentRecord);
        return filename;
    }

    /**
     * delete a document and its record by name to watson Discovery and Lucene
     * @param filename
     * @throws IOException
     */
    public void deleteDocument(String filename) throws IOException {
        DocumentRecord documentRecord = documentRecordRepository.findByName(filename);
        if (documentRecord != null) {
            String documentId = documentRecord.getDiscoveryId();
            watsonDiscovery.deleteDocument(documentId);
            indexer.deleteDocument(filename);
            documentRecordRepository.delete(documentRecord);
        }
    }

}
