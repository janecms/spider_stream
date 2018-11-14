package com.yimilan.elasticsearch.service;

import com.yimilan.elasticsearch.action.SearchRequest;

/**
 * 搜索API
 */
public interface QueryService {

    /**
     * 搜索
     * @param request
     * @return
     */
    public Object search(SearchRequest request);
}
