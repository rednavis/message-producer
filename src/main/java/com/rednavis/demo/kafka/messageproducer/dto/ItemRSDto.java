package com.rednavis.demo.kafka.messageproducer.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemRSDto implements Serializable {
  private String id;
  private String name;
  private String updatedDescription;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime created;
}


