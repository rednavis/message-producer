package com.rednavis.demo.kafka.messageproducer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRQDto;

class SendMessageServiceTest {

  @Mock
  private Processor sourceMock;
  @Mock
  private MessageChannel channelMock;
  @Captor
  private ArgumentCaptor<Message<ItemRQDto>> messageCaptor;

  SendMessageService uut;

  @BeforeEach
  void setUp() {
    initMocks(this);
    uut = new SendMessageService(sourceMock);
    when(sourceMock.output()).thenReturn(channelMock);
  }

  @Test
  void testSendGeneratedMessage() {
    uut.sendGeneratedMessage();

    verify(channelMock, times(1)).send(messageCaptor.capture());
    Message<ItemRQDto> value = messageCaptor.getValue();
    ItemRQDto payload = value.getPayload();
    assertThat(payload).isNotNull();
    assertThat(payload.getName()).startsWith("Item ");
    assertThat(payload.getAmount()).isNotNull().isBetween(0, 1000);
    assertThat(payload.getPrice()).isNotNull().isBetween(0D, 10000D);
    assertThat(payload.getDescription()).isEqualTo("Some basic description.");
    assertThat(payload.isPublic()).isTrue();
  }

  @Test
  void sendUserMessage() {
    ItemRQDto item = ItemRQDto
        .builder()
        .name("name")
        .amount(1)
        .price(20D)
        .isPublic(false)
        .build();

    uut.sendUserMessage(item);

    verify(channelMock, times(1)).send(messageCaptor.capture());
    assertThat(messageCaptor.getValue().getPayload()).isEqualTo(item);
  }
}
