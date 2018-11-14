package com.stream.es.service.impl;

import com.stream.spider.dao.ESClient;
import com.yimilan.elasticsearch.action.IndexResp;
import com.yimilan.elasticsearch.service.IndexService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ElasticIndexService implements IndexService {
    @Autowired
    private ESClient client;

    @Override
    public IndexResp indexDoc(String index, String type, String docId, String jsonSource) {
        IndexResponse response = client.getClient().prepareIndex(index, type, docId)
                .setSource(jsonSource, XContentType.JSON)
                .get();
        IndexResp resp = toResp(response);
        return resp;
    }

    private IndexResp toResp(IndexResponse response) {
        IndexResp resp = new IndexResp();
        resp.setId(response.getId());
        resp.setIndex(response.getIndex());
        resp.setType(response.getType());
        resp.setSeqNo(response.getSeqNo());
        resp.setResult(response.getResult().toString());
        resp.setVersion(response.getVersion());
        return resp;
    }

    public IndexResp indexDoc(String index, String type, String docId, Map<String, ?> source, XContentType contentType) {
        IndexResponse response = client.getClient().prepareIndex(index, type, docId)
                .setSource(source, contentType)
                .get();
        return toResp(response);
    }
}
