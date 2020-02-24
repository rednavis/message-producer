package com.rednavis.demo.kafka.messageproducer.service;

import java.util.Random;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRQDto;
import lombok.AllArgsConstructor;

@Service
@EnableBinding(Processor.class)
@AllArgsConstructor
public class SendMessageService {

  private final Processor processor;
  private final Random rnd = new Random();

  public void sendGeneratedMessage() {
    ItemRQDto item = ItemRQDto.builder()
        .name("Item " + rnd.nextInt())
        .amount(rnd.nextInt(999) + 1)
        .price(rnd.nextDouble() * rnd.nextInt(10000))
        .description("Some basic description.")
        .isPublic(true)
        .build();
    processor.output().send(MessageBuilder.withPayload(item).build());
  }

  public void sendUserMessage(ItemRQDto item) {
    processor.output().send(MessageBuilder.withPayload(item).build());
  }
}
