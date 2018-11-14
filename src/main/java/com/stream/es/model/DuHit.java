package com.stream.es.model;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class DuHit<T> {
    T data;
    float score;
}
