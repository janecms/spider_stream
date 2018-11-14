package com.gs;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.InputStreamStreamInput;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Es1 {

    @Test
    public void test1() throws IOException {

        final MatchQueryBuilder mb2 = QueryBuilders.matchQuery("name", "北京市女子实验中学");
        System.out.println(mb2.toString());

        InputStream is = new ByteArrayInputStream(mb2.toString().getBytes("utf-8"));
/*
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("message", "trying out Elasticsearch")
                .endObject();
        String json = Strings.toString(builder);
        System.out.println(json);
*/
        final WrapperQueryBuilder wrapperQueryBuilder = QueryBuilders.wrapperQuery(mb2.toString());
        StreamInput input = new InputStreamStreamInput(is);
        final MatchQueryBuilder ss = new MatchQueryBuilder(input);
        assertNotNull(ss);
        System.out.println("----------------------");
        System.out.println(ss.toString());
//        XContentBuilder builder = jsonBuilder().startObject().field("match");

    }
}
