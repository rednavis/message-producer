package com.rednavis.demo.kafka.messageproducer.controller;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rednavis.demo.kafka.messageproducer.dto.ItemDto;
import com.rednavis.demo.kafka.messageproducer.service.SendMessageService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MessageController {

  private final SendMessageService sendMessageService;

  @PostMapping("/generated")
  public void postGeneratedMessage() {
    sendMessageService.sendGeneratedMessage();
  }

  @PostMapping("/manual")
  public void postUserMessage(@Valid @RequestBody ItemDto item) {
    sendMessageService.sendUserMessage(item);
  }
}
