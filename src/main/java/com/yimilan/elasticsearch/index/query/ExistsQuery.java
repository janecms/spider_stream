package com.yimilan.elasticsearch.index.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.elasticsearch.common.Strings;

/**
 * @org.elasticsearch.index.query.ExistsQueryBuilder
 */
public class ExistsQuery implements Query{

    private  String fieldName;

    public ExistsQuery() {
    }

    public ExistsQuery(String fieldName) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

