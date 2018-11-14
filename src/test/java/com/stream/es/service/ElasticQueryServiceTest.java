package com.stream.es.service;

import com.stream.SpiderStreamApplication;
import com.stream.es.service.impl.ElasticQueryService;
import com.yimilan.elasticsearch.action.SearchRequest;
import com.yimilan.elasticsearch.index.query.BoolQuery;
import com.yimilan.elasticsearch.index.query.MatchQuery;
import com.yimilan.elasticsearch.index.query.TermQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpiderStreamApplication.class)
public class ElasticQueryServiceTest {

    @Autowired
    ElasticQueryService elasticQueryService;

    /**
     GET yimilan_index_cp/school/_search
     {
       "query": {
         "match_all": {}
       }
     }
     */
    @Test
    public void testMatchall(){
        assertNotNull(elasticQueryService);
        SearchRequest req = new SearchRequest();
        req.setIndice("yimilan_index_cp");
        req.setType("school");
        Object search = elasticQueryService.search(req);
        assertNotNull(search);
    }

    /**

     GET yimilan_index_cp/school/_search
     {
     "query": {
     "match": {
     "name": "船舶七院"
     }
     }
     }

     */
    @Test
    public void test1(){
        assertNotNull(elasticQueryService);
        SearchRequest req = new SearchRequest();
        req.setQuery(new MatchQuery("name","船舶七院"));
        req.setIndice("yimilan_index_cp");
        req.setType("school");
        Object search = elasticQueryService.search(req);
        assertNotNull(search);
    }

    @Test
    public void test2(){
        SearchRequest req = new SearchRequest();
        BoolQuery boolQuery = new BoolQuery();
        boolQuery.addMustClause(new MatchQuery("name","船舶七院"));
        boolQuery.addMustClause(new TermQuery("districtId",37));
        req.setIndice("yimilan_index_cp");
        req.setType("school");
        req.setQuery(boolQuery);
        Object search = elasticQueryService.search(req);
        assertNotNull(search);
    }
}