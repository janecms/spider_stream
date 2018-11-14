package com.yimilan.elasticsearch.index.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @see org.elasticsearch.index.query.TermQueryBuilder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermQuery implements Query {
    String fieldName;
    Object value;
}
