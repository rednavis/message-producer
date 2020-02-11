package com.rednavis.demo.kafka.messageproducer.service;

import java.util.Random;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.rednavis.demo.kafka.messageproducer.dto.ItemDto;
import lombok.AllArgsConstructor;

@Service
@EnableBinding(Source.class)
@AllArgsConstructor
public class SendMessageService {

  private final Source source;
  private final Random rnd = new Random();

  public void sendGeneratedMessage() {
    ItemDto item = ItemDto.builder()
        .name("Item " + rnd.nextInt())
        .amount(rnd.nextInt(1000))
        .price(rnd.nextDouble())
        .description("Some basic description.")
        .isPublic(true)
        .build();
    source.output().send(MessageBuilder.withPayload(item).build());
  }

  public void sendUserMessage(ItemDto item) {
    source.output().send(MessageBuilder.withPayload(item).build());
  }
}
