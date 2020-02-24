package com.rednavis.demo.kafka.messageproducer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.rednavis.demo.kafka.messageproducer.dto.ItemRSDto;

class ListMessageServiceTest {

  private ListMessageService uut;

  @BeforeEach
  void setUp() {
    uut = new ListMessageService();
  }

  @Test
  void testBasicFlow() {
    List<ItemRSDto> items = uut.getItems();

    assertThat(items).isEmpty();

    ItemRSDto itemRSDto = ItemRSDto.builder().id("test id").build();
    uut.listenForItemsAndStoreInCache(itemRSDto);

    items = uut.getItems();
    assertThat(items).hasSize(1);
    assertThat(items.get(0)).isEqualTo(itemRSDto);
  }

  @Test
  void testSizeOfCache() {
    List<ItemRSDto> items = uut.getItems();

    assertThat(items).isEmpty();

    for (int i = 1; i < 20; i++) {
      ItemRSDto itemRSDto = ItemRSDto.builder().id("test id " + i).build();
      uut.listenForItemsAndStoreInCache(itemRSDto);
    }

    await().atMost(200, TimeUnit.MILLISECONDS).untilAsserted(() -> assertThat(uut.getItems()).hasSize(10));
  }
}
