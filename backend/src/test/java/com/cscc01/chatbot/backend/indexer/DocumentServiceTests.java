package com.cscc01.chatbot.backend.indexer;



import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import com.cscc01.chatbot.backend.model.QueryResult;
import com.cscc01.chatbot.backend.querysystem.QuerySystemProcessor;
import com.ibm.watson.discovery.v1.model.DocumentStatus;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.tika.exception.TikaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentServiceTests  {

    @Autowired
    private DocumentService documentService;

    @Inject
    private QuerySystemProcessor querySystemProcessor;

    private File resourcesDirectory = new File("src/main/resources");

    @Test
    public void testDeleteUploadedHtmlFile() throws IOException, TikaException, SAXException, FileTypeNotSupportedException {
        Path path = Paths.get(resourcesDirectory.getAbsolutePath() + "/test/Index.html");
        File file = path.toFile();
        documentService.addFileDocument(file, "someone");
        documentService.deleteDocument(file.getName());
    }

    @Test
    public void testUploadPdfile1() throws FileTypeNotSupportedException, TikaException, SAXException, IOException {
        Path path = Paths.get(resourcesDirectory.getAbsolutePath() + "/test/ChatBotProject.pdf");
        File file = path.toFile();
        documentService.addFileDocument(file, "someone");
        documentService.deleteDocument(file.getName());
    }

    // https://www.utoronto.ca/contacts

    @Test
    public void testDeleteUploadedUrl() throws Exception {
        String filename = documentService.addUrlDocument("https://www.utoronto.ca/contacts", "dfiadmin");
        documentService.deleteDocument(filename);
    }

}