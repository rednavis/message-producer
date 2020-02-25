package com.rednavis.demo.kafka.messageproducer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRSDto;
import com.rednavis.demo.kafka.messageproducer.exception.CacheIsEmptyException;
import lombok.AllArgsConstructor;

@Service
@EnableBinding(Processor.class)
@AllArgsConstructor
public class ListMessageService {

  private final Cache<String, ItemRSDto> cache = Caffeine.newBuilder()
      .expireAfterWrite(20, TimeUnit.MINUTES)
      .maximumSize(10)
      .build();

  public List<ItemRSDto> getItems() {
    if (cache.estimatedSize() == 0) {
      throw new CacheIsEmptyException();
    }
    return new ArrayList<>(cache.asMap().values());
  }

  @StreamListener(Processor.INPUT)
  public void listenForItemsAndStoreInCache(ItemRSDto item) {
    cache.put(item.getId(), item);
  }
}
