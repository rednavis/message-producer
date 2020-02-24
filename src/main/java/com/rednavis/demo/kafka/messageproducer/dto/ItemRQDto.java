package com.rednavis.demo.kafka.messageproducer.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRQDto {

  @NotEmpty(message = "Should not be empty")
  private String name;
  @Min(value = 1, message = "Should be positive")
  private int amount;
  @DecimalMin(value = "0.1", message = "Should be 0.1 or bigger")
  private double price;
  private String description;
  private boolean isPublic;
}
