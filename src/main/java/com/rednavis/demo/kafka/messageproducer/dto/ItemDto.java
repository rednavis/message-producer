package com.rednavis.demo.kafka.messageproducer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {

  private String name;
  private int amount;
  private double price;
  private String description;
  private boolean isPublic;
}
