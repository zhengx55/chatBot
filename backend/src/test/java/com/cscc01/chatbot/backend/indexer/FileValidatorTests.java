package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FileValidatorTests {

    private File resourcesDirectory = new File("src/main/resources");

    private final File pdf = new File(resourcesDirectory.getAbsolutePath() + "/test/ChatBotProject.pdf");
    private final File doc = new File(resourcesDirectory.getAbsolutePath() + "/test/crawler.doc");
    private final File txt = new File(resourcesDirectory.getAbsolutePath() + "/test/Index.txt");
    private final File html = new File(resourcesDirectory.getAbsolutePath() + "/test/Index.html");
    private final File odt = new File(resourcesDirectory.getAbsolutePath() + "/test/notSupported.odt");


    @Inject
    private FileValidator fileValidator;

    @Test
    public void testPdfValidate() throws IOException {
        Assert.assertFalse(fileValidator.isPDF(txt));
        Assert.assertFalse(fileValidator.isPDF(doc));
        Assert.assertTrue(fileValidator.isPDF(pdf));
    }


    @Test
    public void testHtmlValidate() throws IOException, FileTypeNotSupportedException {
        Assert.assertTrue(fileValidator.isValidFile(html));
    }

    @Test
    public void testDocValidate() throws IOException {

        Assert.assertTrue(fileValidator.isDoc(doc));
        Assert.assertFalse(fileValidator.isDoc(pdf));
        Assert.assertFalse(fileValidator.isDoc(txt));
    }


    @Test
    public void testTextValidate() throws IOException {

        Assert.assertFalse(fileValidator.isTxt(doc));
        Assert.assertTrue(fileValidator.isTxt(txt));
        Assert.assertFalse(fileValidator.isTxt(pdf));
    }

    @Test
    public void testFileValidate() throws IOException, FileTypeNotSupportedException {
        boolean notSupported = false;
        Assert.assertTrue(fileValidator.isValidFile(doc));
        Assert.assertTrue(fileValidator.isValidFile(txt));
        Assert.assertTrue(fileValidator.isValidFile(pdf));
        Assert.assertTrue(fileValidator.isValidFile(html));
        try {
            fileValidator.isValidFile(odt);
        } catch (FileTypeNotSupportedException e) {
            notSupported = true;
        }
        Assert.assertTrue(notSupported);


    }

}
