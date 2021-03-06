package com.cscc01.chatbot.backend.sql;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.cscc01.chatbot.backend.model.DocumentRecord;
import com.cscc01.chatbot.backend.sql.repositories.DocumentRecordRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class DocumentRecordRepositoryTest {

    @Autowired
    private DocumentRecordRepository documentRepository;

    @Test
    public void givenDocument_whenSave_thenGetOk() {
        DocumentRecord documentRecord1 = new DocumentRecord("testdoc");
        documentRecord1.setLastModified("2018-09-07");
        documentRepository.save(documentRecord1);
        DocumentRecord documentRecord2 = documentRepository.findByName("testdoc");
        assertEquals("testdoc", documentRecord2.getName());
        assertEquals("2018-09-07", documentRecord2.getLastModified());
    }
}
