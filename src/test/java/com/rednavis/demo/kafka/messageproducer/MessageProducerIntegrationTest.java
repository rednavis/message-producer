package com.rednavis.demo.kafka.messageproducer;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

import java.util.concurrent.BlockingQueue;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRQDto;

@SpringBootTest
@AutoConfigureMockMvc
class MessageProducerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Source source;
  @Autowired
  private MessageCollector messageCollector;

  @Test
  void addItemValid() throws Exception {
    ItemRQDto validItem = ItemRQDto.builder()
        .name("Not empty")
        .amount(15)
        .price(10D)
        .isPublic(false)
        .build();

    String itemRQ = new ObjectMapper().writeValueAsString(validItem);

    mockMvc.perform(MockMvcRequestBuilders
        .post("/manual")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(itemRQ))
        .andExpect(MockMvcResultMatchers.status().isAccepted()).andReturn();

    BlockingQueue<Message<?>> messages = messageCollector.forChannel(source.output());
    assertThat(messages, receivesPayloadThat(is(itemRQ)));
  }

  @Test
  void addItemInvalid() throws Exception {
    ItemRQDto invalidItem = ItemRQDto.builder()
        .name("")
        .amount(0)
        .price(-1D)
        .isPublic(false)
        .build();

    String itemRQ = new ObjectMapper().writeValueAsString(invalidItem);

    String responseString = mockMvc.perform(MockMvcRequestBuilders
        .post("/manual")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(itemRQ))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse().getContentAsString();

    JSONObject jsonObject = new JSONObject(responseString);

    assertEquals("Should be positive", jsonObject.getString("amount"));
    assertEquals("Should not be empty", jsonObject.getString("name"));
    assertEquals("Should be 0.1 or bigger", jsonObject.getString("price"));

    BlockingQueue<Message<?>> messages = messageCollector.forChannel(source.output());
    assertTrue(messages.isEmpty());
  }

  @Test
  void addItemGenerated() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .post("/generated")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

    BlockingQueue<Message<?>> messages = messageCollector.forChannel(source.output());
    assertThat(messages, receivesPayloadThat(containsString("Some basic description.")));
  }

}
