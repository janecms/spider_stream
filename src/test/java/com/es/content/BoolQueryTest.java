package com.es.content;

import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

public class BoolQueryTest {

    public void test1(){
        final TermQueryBuilder tqb = QueryBuilders.termQuery("field1", "value1");
//        tqb.writeTo(ss);
    }
}
