package com.rednavis.demo.kafka.messageproducer.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRQDto;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRSDto;
import com.rednavis.demo.kafka.messageproducer.service.ListMessageService;
import com.rednavis.demo.kafka.messageproducer.service.SendMessageService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MessageController {

  private final SendMessageService sendMessageService;
  private final ListMessageService listMessageService;

  @PostMapping("/generated")
  @ResponseStatus(HttpStatus.OK)
  public void postGeneratedMessage() {
    sendMessageService.sendGeneratedMessage();
  }

  @PostMapping("/manual")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void postUserMessage(@Valid @RequestBody ItemRQDto item) {
    sendMessageService.sendUserMessage(item);
  }

  @GetMapping("/list")
  public List<ItemRSDto> getItemsList() {
    return listMessageService.getItems();
  }
}
