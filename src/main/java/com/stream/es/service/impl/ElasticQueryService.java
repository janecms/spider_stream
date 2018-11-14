package com.stream.es.service.impl;

import com.stream.es.service.EsBuilders;
import com.stream.spider.dao.ESClient;
import com.yimilan.elasticsearch.action.SearchRequest;
import com.yimilan.elasticsearch.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElasticQueryService implements QueryService {
    @Autowired
    private ESClient client;

    @Override
    public Object search(SearchRequest request) {
        assert request.getIndice() != null;
        assert request.getType() != null;
        int from = NumberUtils.toInt(request.getFrom(), 0);
        int size = NumberUtils.toInt(request.getSize(), 20);
        final SearchRequestBuilder srb = client.getClient().prepareSearch(request.getIndice()).setTypes(request.getType()).setFrom(from).setSize(size);
        QueryBuilder queryBuilder = EsBuilders.toQueryBuilder(request.getQuery());
        srb.setQuery(queryBuilder);
        QueryBuilder postFilter = EsBuilders.toQueryBuilder(request.getPostFilter());
        srb.setPostFilter(postFilter);
        if (log.isInfoEnabled()) {
            if (queryBuilder != null) {
                String json = Strings.toString(queryBuilder);
                log.info("queryBuilder:{}", json);
            }
            if (postFilter != null) {
                String filterJson = Strings.toString(postFilter);
                log.info("postFilter:{}", filterJson);
            }
        }
        SearchResponse searchResponse = srb.get();
        SearchHits hits = searchResponse.getHits();
        return hits;
    }

}
