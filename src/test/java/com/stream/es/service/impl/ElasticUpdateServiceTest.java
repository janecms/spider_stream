package com.stream.es.service.impl;

import com.stream.SpiderStreamApplication;
import com.yimilan.elasticsearch.action.IndexResp;
import com.yimilan.elasticsearch.action.UpdateResp;
import com.yimilan.elasticsearch.service.IndexService;
import com.yimilan.elasticsearch.service.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpiderStreamApplication.class)
public class ElasticUpdateServiceTest {
    @Autowired
    UpdateService updateService;
    @Autowired
    IndexService indexService;

    @Test
    public void merge() throws ExecutionException, InterruptedException {
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        IndexResp resp = indexService.indexDoc("yimilan_cp_client", "test", "1", json);
        log.info("创建索引文档 result:{}",resp.getResult());

        String json2 = "{" +
                "\"user\":\"kimchy2\"," +
                "\"postDate\":\"2018-01-30\"," +
                "\"updateTime\":\"trying out Elasticsearch\"" +
                "}";
        UpdateResp merge = updateService.merge("yimilan_cp_client", "test", "1", json2);
        log.info("更新索引文档 result:{}",merge.getResult());
    }
}