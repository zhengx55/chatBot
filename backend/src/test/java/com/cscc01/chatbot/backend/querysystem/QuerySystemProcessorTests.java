package com.cscc01.chatbot.backend.querysystem;


import com.cscc01.chatbot.backend.indexer.WatsonDiscovery;
import com.ibm.watson.assistant.v2.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.MessageOutput;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeIntent;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryResult;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerySystemProcessorTests {

    @Inject
    private QuerySystemProcessor querySystemProcessor;

    @MockBean
    private WatsonAssistant watsonAssistant;

    @MockBean
    private WatsonDiscovery watsonDiscovery;

    private final RuntimeIntent askDiscoveryIntent = Mockito.mock(RuntimeIntent.class);

    private final RuntimeIntent otherIntent = Mockito.mock(RuntimeIntent.class);


    @Test
    public void testQueryAskDiscoveryIntent() throws IOException, ParseException {
        // Mock Watson Discovery responese
        QueryResponse discoveryResponse = Mockito.mock(QueryResponse.class);
        QueryResult queryResult
                = Mockito.mock(QueryResult.class);

        Mockito.when(queryResult.get("text")).thenReturn("discovery result");
        Mockito.when(queryResult.getTitle()).thenReturn("test");
        Mockito.when(discoveryResponse.getResults()).thenReturn(Arrays.asList(queryResult));
        Mockito.doReturn(discoveryResponse)
                .when(watsonDiscovery)
                .query(Mockito.anyString());

        // Mock Watson Assistant response
        MessageResponse response = Mockito.mock(MessageResponse.class);
        MessageOutput output = Mockito.mock(MessageOutput.class);
        DialogRuntimeResponseGeneric generic = Mockito.mock(DialogRuntimeResponseGeneric.class);
        Mockito.doReturn(response)
                .when(watsonAssistant)
                .getMessageResponse(Mockito.anyString(), Mockito.any(), Mockito.any());


        Mockito.when(response.getOutput()).thenReturn(output);
        Mockito.when(output.getIntents()).thenReturn(Arrays.asList(askDiscoveryIntent));

        Mockito.doReturn("askDiscovery")
                .when(askDiscoveryIntent)
                .getIntent();
        Mockito.doReturn("askDiscovery").
                when(generic).getText();

        Assert.assertEquals("discovery result",
                querySystemProcessor.getResponse("What is DFI?").get("content"));


    }

    @Test
    public void testQueryGreetingIntent() throws IOException, ParseException {
        // Mock Watson Assistant response
        MessageResponse response = Mockito.mock(MessageResponse.class);
        MessageOutput output = Mockito.mock(MessageOutput.class);
        DialogRuntimeResponseGeneric generic = Mockito.mock(DialogRuntimeResponseGeneric.class);
        Mockito.doReturn(response)
                .when(watsonAssistant)
                .getMessageResponse(Mockito.anyString(), Mockito.any(), Mockito.any());


        Mockito.when(response.getOutput()).thenReturn(output);
        Mockito.when(output.getIntents()).thenReturn(Arrays.asList(otherIntent));
        Mockito.when(output.getGeneric()).thenReturn(Arrays.asList(generic));

        Mockito.doReturn("greeting").when(otherIntent).getIntent();
        Mockito.doReturn("Hello").
                when(generic).getText();

        Assert.assertEquals("Hello",
                querySystemProcessor.getResponse("Hi.").get("message"));


    }

    @Test
    public void testQueryIntentNotDetected() throws IOException, ParseException {
        // Mock Watson Assistant response
        MessageResponse response = Mockito.mock(MessageResponse.class);
        MessageOutput output = Mockito.mock(MessageOutput.class);
        DialogRuntimeResponseGeneric generic = Mockito.mock(DialogRuntimeResponseGeneric.class);
        Mockito.doReturn(response)
                .when(watsonAssistant)
                .getMessageResponse(Mockito.anyString(), Mockito.any(), Mockito.any());


        Mockito.when(response.getOutput()).thenReturn(output);
        Mockito.when(output.getIntents()).thenReturn(Arrays.asList());
        Mockito.when(output.getGeneric()).thenReturn(Arrays.asList(generic));

        Mockito.doReturn("Hello").when(generic).getText();

        Assert.assertEquals("Hello",
                querySystemProcessor.getResponse("Hasdfmn;d.").get("message"));


    }
}
