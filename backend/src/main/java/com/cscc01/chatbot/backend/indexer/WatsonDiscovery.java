package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.crawler.CrawlerResultKey;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.LogManager;

@Service
public class WatsonDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatsonDiscovery.class);

//    private static final String DEFAULT_CONFIG_NAME = "Default Configuration";

    @Value("${discovery.apikey}")
    private String API_KEY;

    @Value("${discovery.version}")
    private String VERSION;

    @Value("${discovery.url}")
    private String DISCOVERY_URL;

    @Value("${discovery.collection.id}")
    private String COLLECTION_ID;

    @Value("${discovery.configuration.id}")
    private String CONFIG_ID;

    @Value("${discovery.environment.id}")
    private String ENV_ID;

    private Discovery discovery;

    private final FileValidator fileValidator;

    public WatsonDiscovery(FileValidator fileValidator) {
        this.fileValidator = fileValidator;
    }


    /**
     * create a bean instance for IBM watson discovery
     * @return
     */
    @Bean
    public Discovery createDiscovery() {
        IamOptions options = new IamOptions.Builder()
                .apiKey(API_KEY)
                .build();
        Discovery discovery = new Discovery(VERSION, options);
        discovery.setEndPoint(DISCOVERY_URL);
        this.discovery = discovery;
        return discovery;
    }

    /**
     * query IBM watson discovery by given test (using natural language processing)
     * @param text
     * @return
     */
    public QueryResponse query(String text) {
        LogManager.getLogManager().reset();
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(ENV_ID, COLLECTION_ID);

        queryBuilder.deduplicate(true).naturalLanguageQuery(text).count(3).highlight(true);
        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute().getResult();
        return queryResponse;
    }

    public QueryResponse query1() {
        LogManager.getLogManager().reset();
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(ENV_ID, COLLECTION_ID);

        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute().getResult();
        return queryResponse;
    }


    /**
     * add a file document to discovery
     * @param file
     * @param existDocumentId
     * @return
     * @throws IOException
     */
    public DocumentAccepted addDocument(File file, String existDocumentId) throws IOException {
        LogManager.getLogManager().reset();
//        checkEnvironment();
//        checkCollection();

        UpdateDocumentOptions.Builder builder
                = new UpdateDocumentOptions.Builder(ENV_ID, COLLECTION_ID, existDocumentId);
//        if (fileValidator.isPDF(file)) {
//            builder.fileContentType(HttpMediaType.APPLICATION_PDF);
//        } else if (fileValidator.isDoc(file)) {
//            builder.fileContentType(HttpMediaType.APPLICATION_MS_WORD);
//        } else if (fileValidator.isTxt(file)) {
//            builder.fileContentType(HttpMediaType.TEXT_PLAIN);
//        }
        builder.filename(file.getName());
        DocumentAccepted response = discovery.updateDocument(builder.file(new FileInputStream(file)).build()).execute().getResult();
        String documentId = response.getDocumentId();
        LOGGER.info("Created a document ID: " + documentId);
        if (checkDocument(documentId)) {
            LOGGER.info("Document Ready.");
        } else {
            LOGGER.info("Document upload error.");

        }
        return response;
    }

    /**
     * add a url crawled result to discovery
     * @param crawlerResult
     * @param existDocumentId
     * @return
     * @throws IOException
     */
    public DocumentAccepted addDocument(Map<CrawlerResultKey, String> crawlerResult, String existDocumentId) throws IOException {
        LogManager.getLogManager().reset();
//        checkEnvironment();
//        checkCollection();
        InputStream file
                = new ByteArrayInputStream(crawlerResult.get(CrawlerResultKey.HTML).getBytes(StandardCharsets.UTF_8));
        UpdateDocumentOptions.Builder builder
                = new UpdateDocumentOptions.Builder(ENV_ID, COLLECTION_ID, existDocumentId);
        builder.filename(crawlerResult.get(CrawlerResultKey.TITLE));
        builder.file(file);
        builder.fileContentType(HttpMediaType.TEXT_HTML);
        DocumentAccepted response = discovery.updateDocument(builder.build()).execute().getResult();
        String documentId = response.getDocumentId();
        LOGGER.info("Created a document ID: " + documentId);
        checkDocument(documentId);
        return response;
    }

    /**
     * delete a document by id from discovery
     * @param existDocumentId
     */
    public void deleteDocument(String existDocumentId) {
        LogManager.getLogManager().reset();
//        checkEnvironment();
//        checkCollection();

        DeleteDocumentOptions deleteRequest
                = new DeleteDocumentOptions.Builder(ENV_ID, COLLECTION_ID, existDocumentId).build();
        DeleteDocumentResponse deleteResponse
                = discovery.deleteDocument(deleteRequest).execute().getResult();
        LOGGER.info("Delete document from discovery, document ID: " + deleteResponse.getDocumentId() + " status: " + deleteResponse.getStatus());
    }


    /**
     * check if discovery environment is ok
     */
    public void checkEnvironment() {
        String environmentId = null;
        LOGGER.info("Check if environment exists");
        ListEnvironmentsOptions listOptions = new ListEnvironmentsOptions.Builder().build();
        ListEnvironmentsResponse listResponse = discovery.listEnvironments(listOptions).execute().getResult();
        for (Environment environment : listResponse.getEnvironments()) {
            if (!environment.isReadOnly()) {
                environmentId = environment.getEnvironmentId();
                if (environmentId.equals(ENV_ID)) {
                    LOGGER.info("Found existing environment ID: " + environmentId);
                    break;
                }
            }
        }

        if (environmentId == null) {
            LOGGER.info("No environment found, creating new one...");
            String environmentName = "chatbot-dev";
            CreateEnvironmentOptions createOptions = new CreateEnvironmentOptions.Builder()
                    .name(environmentName)
                    .size("LT")
                    .build();
            Environment createResponse = discovery.createEnvironment(createOptions).execute().getResult();
            environmentId = createResponse.getEnvironmentId();
            LOGGER.info("Created new environment ID: " + environmentId);
            this.ENV_ID = environmentId;
            LOGGER.info("Waiting for environment to be ready...");
        }

        boolean environmentReady = false;
        while (!environmentReady) {
            GetEnvironmentOptions getEnvironmentOptions = new GetEnvironmentOptions.Builder(environmentId).build();
            Environment getEnvironmentResponse = discovery.getEnvironment(getEnvironmentOptions).execute().getResult();
            environmentReady = getEnvironmentResponse.getStatus().equals(Environment.Status.ACTIVE);
            try {
                if (!environmentReady) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }

            LOGGER.info("Environment Ready!");
        }
    }

    /**
     * check if discovery collection is ok
     */
    public void checkCollection() {
        LOGGER.info("Waiting for collection to be ready...");
        boolean collectionReady = false;
        while (!collectionReady) {
            GetCollectionOptions getCollectionOptions =
                    new GetCollectionOptions.Builder(ENV_ID, COLLECTION_ID).build();
            Collection getCollectionResponse = discovery.getCollection(getCollectionOptions).execute().getResult();
            collectionReady = getCollectionResponse.getStatus().equals(Collection.Status.ACTIVE);
            try {
                if (!collectionReady) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }
        }
        LOGGER.info("Collection Ready!");
    }

    /**
     * check if the document is available to query
     * @param documentId
     * @return
     */
    public boolean checkDocument(String documentId) {
        LOGGER.info("Waiting for document to be ready...");
        boolean documentReady = false;
        boolean documentFailed = false;
        while (!documentReady) {
            GetDocumentStatusOptions getDocumentStatusOptions =
                    new GetDocumentStatusOptions.Builder(ENV_ID, COLLECTION_ID, documentId).build();
            DocumentStatus getDocumentResponse = discovery.getDocumentStatus(getDocumentStatusOptions).execute().getResult();
            documentReady = !getDocumentResponse.getStatus().equals(DocumentStatus.Status.PROCESSING);
            if (getDocumentResponse.getStatus().equals(DocumentStatus.Status.FAILED)) {
                documentFailed = true;
                break;
            }
            try {
                if (!documentReady) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted");
            }
        }
        return !documentFailed;
    }

    /**
     * get a document status by id
     * @param documentId
     * @return
     */
    public DocumentStatus getDocumentStatus(String documentId) {
        GetDocumentStatusOptions getDocumentStatusOptions =
                new GetDocumentStatusOptions.Builder(ENV_ID, COLLECTION_ID, documentId).build();
        return discovery.getDocumentStatus(getDocumentStatusOptions).execute().getResult();
    }

}
