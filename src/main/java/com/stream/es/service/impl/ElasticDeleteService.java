package com.stream.es.service.impl;

import com.stream.es.service.EsBuilders;
import com.yimilan.elasticsearch.action.DeleteResp;
import com.yimilan.elasticsearch.service.DeleteService;
import com.stream.spider.dao.ESClient;
import com.yimilan.elasticsearch.index.query.Query;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticDeleteService implements DeleteService {
    @Autowired
    private ESClient client;
    @Override
    public DeleteResp delete(String index, String type, String docId) {
        DeleteResponse response = client.getClient().prepareDelete(index, type, docId).get();
        DeleteResp resp = new DeleteResp();
        resp.setId(response.getId());
        resp.setIndex(response.getIndex());
        resp.setType(response.getType());
        resp.setVersion(response.getVersion());
        resp.setResult(response.getResult().toString());
        return resp;


    }

    @Override
    public long delete(String index, String type, Query query) {
        QueryBuilder queryBuilder = EsBuilders.toQueryBuilder(query);
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client.getClient())
                .filter(queryBuilder)
                .source(index)
                .get();
        long deleted = response.getDeleted();
        return deleted;
    }
}
