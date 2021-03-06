package com.cscc01.chatbot.backend.indexer;

import com.cscc01.chatbot.backend.indexer.exception.FileTypeNotSupportedException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

@Component
public class FileValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileValidator.class);

    public static final HashSet<String> SUPPORTED_FILE_TYPE = new HashSet<>();
    private static final String MIMETYPE_PDF = "application/pdf";
    private static final String MIMETYPE_HTML = "text/html";
    private static final String MIMETYPE_TEXT = "text/plain";
    private static final String MIMETYPE_DOC = "application/msword";
    private static final String MIMETYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public FileValidator() {
        SUPPORTED_FILE_TYPE.add(MIMETYPE_DOC);
        SUPPORTED_FILE_TYPE.add(MIMETYPE_DOCX);
        SUPPORTED_FILE_TYPE.add(MIMETYPE_HTML);
        SUPPORTED_FILE_TYPE.add(MIMETYPE_PDF);
        SUPPORTED_FILE_TYPE.add(MIMETYPE_TEXT);
    }

    /**
     * validate a given file type
     * @param file
     * @return
     * @throws IOException
     * @throws FileTypeNotSupportedException
     */
    public boolean isValidFile(File file) throws IOException, FileTypeNotSupportedException {
        Tika tika = new Tika();
        String fileType = tika.detect(file);
        if (SUPPORTED_FILE_TYPE.contains(fileType)) {
            return true;
        } else {
            throw new FileTypeNotSupportedException(fileType);
        }
    }

    /**
     * check if given file is pdf
     * @param file
     * @return
     * @throws IOException
     */
    public boolean isPDF(File file) throws IOException {
        return new Tika().detect(file).equals("application/pdf");
    }

    /**
     * check if given file is text
     * @param file
     * @return
     * @throws IOException
     */
    public boolean isTxt(File file) throws IOException {
        return new Tika().detect(file).equals("text/plain");
    }

    /**
     * check if given file is doc/docx
     * @param file
     * @return
     * @throws IOException
     */
    public boolean isDoc(File file) throws IOException {
        String fileType = new Tika().detect(file);
        return (fileType.equals(MIMETYPE_DOCX) || fileType.equals(MIMETYPE_DOC));
    }
}
