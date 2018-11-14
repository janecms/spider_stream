package com.yimilan.elasticsearch.service;

import com.yimilan.elasticsearch.action.IndexResp;

/**
 * Index API
 */
public interface IndexService {

    public IndexResp indexDoc(String index, String type, String docId, String jsonSource);
}
